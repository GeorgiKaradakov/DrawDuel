package com.drawduel.infrastructure.api;

import com.drawduel.application.dtos.UpdateProfileRequestDto;
import com.drawduel.application.ports.DeleteAccountUseCasePort;
import com.drawduel.application.ports.GetProfileUseCasePort;
import com.drawduel.application.ports.UpdateProfileUseCasePort;
import com.drawduel.application.services.JwtService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final UpdateProfileUseCasePort updateProfileUseCase;
  private final DeleteAccountUseCasePort deleteAccountUseCase;
  private final JwtService jwtService;
  private final GetProfileUseCasePort getProfileUseCase;

  @PutMapping("/profile")
  public ResponseEntity<?> updateProfile(
      @RequestBody UpdateProfileRequestDto request,
      @RequestHeader("Authorization") String authHeader) {

    UUID userId = jwtService.extractUserId(authHeader.substring(7));

    var result =
        updateProfileUseCase.handle(
            new UpdateProfileUseCasePort.Query(
                userId, request.getUsername(), request.getEmail(), null));

    return ResponseEntity.ok(result);
  }

  @GetMapping("/me")
  public Object getMyProfile(@RequestHeader("Authorization") String authHeader) {
    UUID userId = jwtService.extractUserId(authHeader.substring(7));

    return getProfileUseCase.handle(new GetProfileUseCasePort.Query(userId)).profile();
  }

  @DeleteMapping
  public ResponseEntity<?> deleteAccount(@RequestHeader("Authorization") String authHeader) {

    UUID userId = jwtService.extractUserId(authHeader.substring(7));
    deleteAccountUseCase.handle(new DeleteAccountUseCasePort.Query(userId));

    return ResponseEntity.noContent().build();
  }
}
