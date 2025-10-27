package com.drawduel.infrastructure.api;

import com.drawduel.application.dtos.AuthResponseDto;
import com.drawduel.application.dtos.RegisterRequestDto;
import com.drawduel.application.ports.LoginUseCasePort;
import com.drawduel.application.ports.RegisterUseCasePort;
import jakarta.servlet.http.HttpServletRequest;
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

    var accessToken = registerUseCase.handle(new RegisterUseCasePort.Query(req));
    return ResponseEntity.ok(accessToken.response());
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
