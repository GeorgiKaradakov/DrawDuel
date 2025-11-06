package com.drawduel.infrastructure.api;

import com.drawduel.application.dtos.AuthResponseDto;
import com.drawduel.application.dtos.RegisterRequestDto;
import com.drawduel.application.ports.LoginUseCasePort;
import com.drawduel.application.ports.RegisterUseCasePort;
import jakarta.servlet.http.HttpServletRequest;
import java.net.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final RegisterUseCasePort registerUseCase;
  private final LoginUseCasePort loginUseCase;

  public AuthController(RegisterUseCasePort registerUseCase, LoginUseCasePort loginUseCase) {
    this.registerUseCase = registerUseCase;
    this.loginUseCase = loginUseCase;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponseDto> register(
      @RequestBody RegisterRequestDto req, HttpServletRequest request) {
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
    request.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    return ResponseEntity.ok(tokens.response().getAccessToken());
    // return ResponseEntity.ok(accessToken);
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
