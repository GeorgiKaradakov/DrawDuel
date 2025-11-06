package com.drawduel.infrastructure.api;

import com.drawduel.application.dtos.RegisterRequestDto;
import com.drawduel.application.dtos.UserDto;
import com.drawduel.application.ports.GetRefreshTokenUseCasePort;
import com.drawduel.application.ports.GetUserByIdUseCasePort;
import com.drawduel.application.ports.GetRefreshTokenUseCasePort.Query;
import com.drawduel.application.ports.GetRefreshTokenUseCasePort.Result;
import com.drawduel.application.services.TokensService;
import com.drawduel.application.ports.LoginUseCasePort;
import com.drawduel.application.ports.RegisterUseCasePort;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final RegisterUseCasePort registerUseCase;
  private final LoginUseCasePort loginUseCase;
  private final GetRefreshTokenUseCasePort getRefreshTokenUseCase;
  private final GetUserByIdUseCasePort getUserByIdUseCase;
  private final TokensService tokensService;

  public record AccessTokenDto(String accessToken) {}

  public AuthController(
      RegisterUseCasePort registerUseCase,
      LoginUseCasePort loginUseCase,
      GetRefreshTokenUseCasePort getRefreshTokenUseCase,GetUserByIdUseCasePort getUserByIdUseCase,TokensService tokensService) {
    this.registerUseCase = registerUseCase;
    this.loginUseCase = loginUseCase;
    this.getRefreshTokenUseCase = getRefreshTokenUseCase;
    this.getUserByIdUseCase = getUserByIdUseCase;
    this.tokensService = tokensService;
  }

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
    // var accessToken = new AuthResponseDto("mocked_token_for_registration");
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
    // return ResponseEntity.ok(accessToken);
  }

  @GetMapping("/refresh-tokens")
  public ResponseEntity<AccessTokenDto> refreshTokens(
      HttpServletRequest request, HttpServletResponse res) {
    String refreshToken = extractCookie(request);
    if (refreshToken == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

  Result session;
	try {
      session = getRefreshTokenUseCase.handle(new Query(refreshToken))
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
    if (session.getRevoked() || session.getExpiresAt().isBefore(Instant.now()))
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    UserDto user;
    try{
      user = getUserByIdUseCase.handle(session.getUserId());
    }catch(Exception e){
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String[] tokens = tokensService.generateTokens(user, session.getIpAddress(), session.getUserAgent(), session.getLocation());
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
