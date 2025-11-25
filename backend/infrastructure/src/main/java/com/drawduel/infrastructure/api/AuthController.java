package com.drawduel.infrastructure.api;

import com.drawduel.application.dtos.LoginRequestDto;
import com.drawduel.application.dtos.RegisterRequestDto;
import com.drawduel.application.dtos.UserDto;
import com.drawduel.application.dtos.UserSessionDto;
import com.drawduel.application.ports.GetRefreshTokenUseCasePort;
import com.drawduel.application.ports.GetUserByIdUseCasePort;
import com.drawduel.application.ports.LoginUseCasePort;
import com.drawduel.application.ports.RegisterUseCasePort;
import com.drawduel.application.ports.RevokeRefreshTokenUseCasePort;
import com.drawduel.application.services.TokensService;
import com.drawduel.application.usecases.GetRefreshTokenUseCase;
import com.drawduel.application.usecases.GetUserByIdUseCase;
import com.drawduel.application.usecases.LoginUseCase;
import com.drawduel.application.usecases.RevokeRefreshTokenUseCase;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
  public record AccessTokenDto(String accessToken) {}

  private final RegisterUseCasePort registerUseCase;
  private final LoginUseCasePort loginUseCase;
  private final GetUserByIdUseCasePort getUserByIdUseCase;
  private final RevokeRefreshTokenUseCasePort revokeRefreshTokenUseCase;
  private final GetRefreshTokenUseCasePort getRefreshTokenUseCase;
  private final TokensService tokensService;

  @PostMapping("/register")
  public ResponseEntity<AccessTokenDto> register(
      @RequestBody RegisterRequestDto req, HttpServletResponse res, HttpServletRequest request) {
    String Ip = extractClientIp(request);
    String userAgent = request.getHeader("User-Agent");
    String location = "unknown";

    req.setIpAddress(Ip);
    req.setUserAgent(userAgent);
    req.setLocation(location);

    var tokens = registerUseCase.handle(new RegisterUseCasePort.Query(req));
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

    LoginUseCase.Result tokens =
        loginUseCase.handle(
            new LoginUseCase.Query(
                req.getIdentifier(), req.getPassword(), Ip, userAgent, location));

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
            user, session.getIpAddress(), session.getUserAgent(), session.getLocation());
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
}
