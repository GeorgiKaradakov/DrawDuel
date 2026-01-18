package com.drawduel.infrastructure.api;

import com.drawduel.application.dtos.LoginRequestDto;
import com.drawduel.application.dtos.RegisterRequestDto;
import com.drawduel.application.dtos.UserDto;
import com.drawduel.application.dtos.UserSessionDto;
import com.drawduel.application.ports.GetRefreshTokenUseCasePort;
import com.drawduel.application.ports.GetUserByIdUseCasePort;
import com.drawduel.application.ports.GetUserInfoUseCasePort;
import com.drawduel.application.ports.LoginUseCasePort;
import com.drawduel.application.ports.LogoutUseCasePort;
import com.drawduel.application.ports.RegisterUseCasePort;
import com.drawduel.application.ports.RevokeRefreshTokenUseCasePort;
import com.drawduel.application.services.TokensService;
import com.drawduel.application.usecases.GetRefreshTokenUseCase;
import com.drawduel.application.usecases.GetUserByIdUseCase;
import com.drawduel.application.usecases.LoginUseCase;
import com.drawduel.application.usecases.RevokeRefreshTokenUseCase;
import com.drawduel.infrastructure.persistence.security.SecurityUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ua_parser.Client;
import ua_parser.Parser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
  public record AccessTokenDto(String accessToken) {}

  private final RegisterUseCasePort registerUseCase;
  private final LoginUseCasePort loginUseCase;
  private final GetUserByIdUseCasePort getUserByIdUseCase;
  private final GetUserInfoUseCasePort getUserInfoUseCase;
  private final RevokeRefreshTokenUseCasePort revokeRefreshTokenUseCase;
  private final GetRefreshTokenUseCasePort getRefreshTokenUseCase;
  private final TokensService tokensService;
  private final LogoutUseCasePort logoutUseCase;

  private static final Parser parser = new Parser();

  @PostMapping("/register")
  public ResponseEntity<AccessTokenDto> register(
      @ModelAttribute RegisterRequestDto req,
      @RequestParam(required = false) MultipartFile profileImage,
      HttpServletResponse res,
      HttpServletRequest request) {
    String Ip = extractClientIp(request);
    String userAgent = request.getHeader("User-Agent");
    String location = "unknown";
    String osName = System.getProperty("os.name");
    byte[] imageBytes = null;

    Client client = parser.parse(userAgent);
    String browserName = client.userAgent.family;

    if (profileImage != null && !profileImage.isEmpty()) {
      try {
        imageBytes = profileImage.getBytes();
      } catch (Exception e) {
        throw new IllegalArgumentException("Failed to read profile image", e);
      }
    }

    req.setIpAddress(Ip);
    req.setUserAgent(browserName);
    req.setLocation(location);
    req.setOsName(osName);

    var tokens = registerUseCase.handle(new RegisterUseCasePort.Query(req, imageBytes));
    ResponseCookie cookie =
        ResponseCookie.from("refreshToken", tokens.response().getRefreshToken())
            .httpOnly(true)
            .secure(true)
            .sameSite("Strict")
            .path("/")
            .maxAge(7 * 24 * 60 * 60)
            .build();
    res.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, cookie.toString());

    return ResponseEntity.ok(new AccessTokenDto(tokens.response().getAccessToken()));
  }

  @PostMapping("/login")
  public ResponseEntity<AccessTokenDto> login(
      @RequestBody LoginRequestDto req, HttpServletResponse res, HttpServletRequest request) {
    String Ip = extractClientIp(request);
    String userAgent = request.getHeader("User-Agent");
    String location = "unknown";
    String osName = System.getProperty("os.name");

    Client client = parser.parse(userAgent);
    String browserName = client.userAgent.family;

    LoginUseCase.Result tokens =
        loginUseCase.handle(
            new LoginUseCase.Query(
                req.getIdentifier(), req.getPassword(), Ip, browserName, location, osName));

    System.out.println("does it come here");

    ResponseCookie cookie =
        ResponseCookie.from("refreshToken", tokens.refreshToken())
            .httpOnly(true)
            .secure(true)
            .sameSite("Strict")
            .path("/")
            .maxAge(7 * 24 * 60 * 60)
            .build();
    res.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, cookie.toString());

    return ResponseEntity.ok(new AccessTokenDto(tokens.accessToken()));
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> getUserInfo(Authentication authentication) {
    if (authentication == null || !(authentication.getPrincipal() instanceof SecurityUser)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    SecurityUser principal = (SecurityUser) authentication.getPrincipal();

    UserDto user;
    try {
      user =
          getUserInfoUseCase.handle(new GetUserInfoUseCasePort.Query(principal.getId())).response();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    return ResponseEntity.ok(user);
  }

  @PostMapping("/refresh-tokens")
  public ResponseEntity<AccessTokenDto> refreshTokens(
      HttpServletRequest request, HttpServletResponse res) {
    String refreshToken = extractCookie(request);
    if (refreshToken == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    UserSessionDto session;
    try {
      session =
          getRefreshTokenUseCase.handle(new GetRefreshTokenUseCase.Query(refreshToken)).response();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    if (session.getRevoked() || session.getExpiresAt().isBefore(Instant.now()))
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    UserDto user;
    try {
      user =
          getUserByIdUseCase.handle(new GetUserByIdUseCase.Query(session.getUserId())).response();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    revokeRefreshTokenUseCase.handle(new RevokeRefreshTokenUseCase.Query(refreshToken));

    String[] tokens =
        tokensService.generateTokens(
            user,
            session.getSessionId(),
            session.getIpAddress(),
            session.getUserAgent(),
            session.getLocation(),
            session.getOsName());
    ResponseCookie cookie =
        ResponseCookie.from("refreshToken", tokens[1])
            .httpOnly(true)
            .secure(true)
            .sameSite("Strict")
            .path("/")
            .maxAge(7 * 24 * 60 * 60)
            .build();
    res.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, cookie.toString());

    return ResponseEntity.ok(new AccessTokenDto(tokens[0]));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
    String refreshToken = extractRefreshToken(request);

    logoutUseCase.handle(new LogoutUseCasePort.Query(refreshToken));

    deleteRefreshTokenCookie(response);

    return ResponseEntity.noContent().build();
  }

  private String extractCookie(HttpServletRequest req) {
    if (req.getCookies() == null) return null;

    for (Cookie c : req.getCookies()) {
      if ("refreshToken".equals(c.getName())) {
        return c.getValue();
      }
    }

    return null;
  }

  private String extractClientIp(HttpServletRequest request) {
    String forwarded = request.getHeader("X-Forwarded-For");
    if (forwarded != null && !forwarded.isEmpty()) {
      return forwarded.split(",")[0];
    }

    String ip = request.getRemoteAddr();
    if ("0:0:0:0:0:0:0:1".equals(ip)) {
      return "127.0.0.1";
    }

    return request.getRemoteAddr();
  }

  private String extractRefreshToken(HttpServletRequest request) {
    if (request.getCookies() == null) return null;

    for (Cookie cookie : request.getCookies()) {
      if ("refreshToken".equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }

  private void deleteRefreshTokenCookie(HttpServletResponse response) {
    Cookie cookie = new Cookie("refreshToken", "");
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    response.addCookie(cookie);
  }
}
