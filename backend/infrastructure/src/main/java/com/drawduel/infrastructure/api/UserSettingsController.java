package com.drawduel.infrastructure.api;

import com.drawduel.application.dtos.ChangePasswordRequestDto;
import com.drawduel.application.dtos.DevicesResponseDto;
import com.drawduel.application.dtos.UpdateUserDetailsRequestDto;
import com.drawduel.application.ports.ChangePasswordUseCasePort;
import com.drawduel.application.ports.DeleteAccountUseCasePort;
import com.drawduel.application.ports.DeleteProfileImageUseCasePort;
import com.drawduel.application.ports.GetProfileUseCasePort;
import com.drawduel.application.ports.GetSessionInfoUseCasePort;
import com.drawduel.application.ports.RevokeSessionUseCasePort;
import com.drawduel.application.ports.UpdateProfileUseCasePort;
import com.drawduel.application.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserSettingsController {

  private final RevokeSessionUseCasePort revokeSessionUseCase;
  private final UpdateProfileUseCasePort updateProfileUseCase;
  private final DeleteAccountUseCasePort deleteAccountUseCase;
  private final ChangePasswordUseCasePort changePasswordUseCase;
  private final JwtService jwtService;
  private final GetProfileUseCasePort getProfileUseCase;
  private final GetSessionInfoUseCasePort getSessionInfoUseCase;
  private final DeleteProfileImageUseCasePort deleteProfileImageUseCase;

  @PatchMapping("/update-username")
  public ResponseEntity<?> updateUsername(
      @RequestHeader("Authorization") String authHeader,
      @RequestBody UpdateUserDetailsRequestDto request) {
    UUID userId = jwtService.extractUserId(authHeader.substring(7));
    String newUsername = request.getValue();

    var result =
        updateProfileUseCase.handle(
            new UpdateProfileUseCasePort.Query(userId, newUsername, null, null));

    return ResponseEntity.ok(result.username());
  }

  @PatchMapping("/update-email")
  public ResponseEntity<?> updateEmail(
      @RequestHeader("Authorization") String authHeader,
      @RequestBody UpdateUserDetailsRequestDto request) {

    UUID userId = jwtService.extractUserId(authHeader.substring(7));
    String newEmail = request.getValue();

    var result =
        updateProfileUseCase.handle(
            new UpdateProfileUseCasePort.Query(userId, null, newEmail, null));

    return ResponseEntity.ok(result.email());
  }

  @PostMapping("/update-profile-image")
  public ResponseEntity<?> updateProfileImage(
      @RequestHeader("Authorization") String authHeader,
      @RequestParam("profileImage") MultipartFile profileImage) {

    UUID userId = jwtService.extractUserId(authHeader.substring(7));

    byte[] imageBytes = null;
    if (profileImage != null && !profileImage.isEmpty()) {
      try {
        imageBytes = profileImage.getBytes();
      } catch (Exception e) {
        throw new IllegalArgumentException("Failed to read profile image", e);
      }
    }

    var result =
        updateProfileUseCase.handle(
            new UpdateProfileUseCasePort.Query(userId, null, null, imageBytes));

    return ResponseEntity.ok(result.email());
  }

  @PostMapping("/change-password")
  public ResponseEntity<?> changePassword(
      @RequestHeader("Authorization") String authHeader,
      @RequestBody ChangePasswordRequestDto request) {
    UUID userId = jwtService.extractUserId(authHeader.substring(7));
    String currentPassword = request.getCurrentPassword();
    String newPassword = request.getNewPassword();

    changePasswordUseCase.handle(
        new ChangePasswordUseCasePort.Query(userId, currentPassword, newPassword));

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/get-devices")
  public ResponseEntity<?> getDevices(
      @RequestHeader("Authorization") String authHeader, HttpServletRequest request) {
    String accessToken = authHeader.substring(7);
    UUID userId = jwtService.extractUserId(accessToken);
    UUID sessionId = jwtService.extractSessionIdAllowExpired(accessToken);

    DevicesResponseDto devices =
        getSessionInfoUseCase
            .handle(new GetSessionInfoUseCasePort.Query(userId, sessionId))
            .devices();

    return ResponseEntity.ok(devices);
  }

  @DeleteMapping("/revoke-session/{sessionId}")
  public ResponseEntity<?> revokeSession(
      @PathVariable("sessionId") UUID sessionId,
      @RequestHeader("Authorization") String authHeader) {

    String accessToken = authHeader.substring(7);
    UUID userId = jwtService.extractUserId(accessToken);
    UUID currentSessionId = jwtService.extractSessionIdAllowExpired(accessToken);

    revokeSessionUseCase.handle(
        new RevokeSessionUseCasePort.Query(userId, sessionId, currentSessionId));

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/delete-profile-image")
  public ResponseEntity<?> deleteProfileImage(@RequestHeader("Authorization") String authHeader) {
    UUID userId = jwtService.extractUserId(authHeader.substring(7));

    String result =
        deleteProfileImageUseCase
            .handle(new DeleteProfileImageUseCasePort.Query(userId))
            .profileImageUrl();

    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/delete-account")
  public ResponseEntity<?> deleteAccount(
      @RequestHeader("Authorization") String authHeader, HttpServletResponse response) {

    UUID userId = jwtService.extractUserId(authHeader.substring(7));
    deleteAccountUseCase.handle(new DeleteAccountUseCasePort.Query(userId));

    deleteRefreshTokenCookie(response);

    return ResponseEntity.noContent().build();
  }

  private void deleteRefreshTokenCookie(HttpServletResponse response) {
    Cookie cookie = new Cookie("refreshToken", "");
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    response.addCookie(cookie);
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
