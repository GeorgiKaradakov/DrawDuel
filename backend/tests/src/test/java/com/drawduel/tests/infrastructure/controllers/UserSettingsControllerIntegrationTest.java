package com.drawduel.tests.infrastructure.controllers;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.drawduel.application.dtos.DeviceEntryDto;
import com.drawduel.application.dtos.DevicesResponseDto;
import com.drawduel.application.ports.*;
import com.drawduel.application.services.JwtService;
import com.drawduel.domain.enums.DeviceStatus;
import com.drawduel.tests.BaseIntegrationTest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc(addFilters = false)
class UserSettingsControllerTest extends BaseIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private RevokeSessionUseCasePort revokeSessionUseCase;
  @MockBean private UpdateProfileUseCasePort updateProfileUseCase;
  @MockBean private DeleteAccountUseCasePort deleteAccountUseCase;
  @MockBean private ChangePasswordUseCasePort changePasswordUseCase;
  @MockBean private GetProfileUseCasePort getProfileUseCase;
  @MockBean private GetSessionInfoUseCasePort getSessionInfoUseCase;
  @MockBean private JwtService jwtService;

  private static final String AUTH_HEADER = "Bearer faketoken";

  @Test
  void updateUsername_returnsOk() throws Exception {
    UUID userId = UUID.randomUUID();

    when(jwtService.extractUserId("faketoken")).thenReturn(userId);
    when(updateProfileUseCase.handle(any()))
        .thenReturn(new UpdateProfileUseCasePort.Result("newUser", null, null));

    mockMvc
        .perform(
            patch("/api/user/update-username")
                .header("Authorization", AUTH_HEADER)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"value\":\"newUser\"}"))
        .andExpect(status().isOk())
        .andExpect(content().string("newUser"));
  }

  @Test
  void updateEmail_returnsOk() throws Exception {
    UUID userId = UUID.randomUUID();

    when(jwtService.extractUserId("faketoken")).thenReturn(userId);
    when(updateProfileUseCase.handle(any()))
        .thenReturn(new UpdateProfileUseCasePort.Result(null, "new@email.com", null));

    mockMvc
        .perform(
            patch("/api/user/update-email")
                .header("Authorization", AUTH_HEADER)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"value\":\"new@email.com\"}"))
        .andExpect(status().isOk())
        .andExpect(content().string("new@email.com"));
  }

  @Test
  void updateProfileImage_returnsOk() throws Exception {
    UUID userId = UUID.randomUUID();

    when(jwtService.extractUserId("faketoken")).thenReturn(userId);
    when(updateProfileUseCase.handle(any()))
        .thenReturn(new UpdateProfileUseCasePort.Result(null, null, "new-image-url"));

    MockMultipartFile file =
        new MockMultipartFile(
            "profileImage",
            "avatar.png",
            MediaType.IMAGE_PNG_VALUE,
            "fake-image".getBytes(StandardCharsets.UTF_8));

    mockMvc
        .perform(
            multipart("/api/user/update-profile-image")
                .file(file)
                .header("Authorization", AUTH_HEADER))
        .andExpect(status().isOk());
  }

  @Test
  void changePassword_returnsNoContent() throws Exception {
    UUID userId = UUID.randomUUID();

    when(jwtService.extractUserId("faketoken")).thenReturn(userId);

    mockMvc
        .perform(
            post("/api/user/change-password")
                .header("Authorization", AUTH_HEADER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "currentPassword": "oldPass",
                      "newPassword": "newPass"
                    }
                    """))
        .andExpect(status().isNoContent());
  }

  @Test
  void getDevices_returnsDevices() throws Exception {
    UUID userId = UUID.randomUUID();
    UUID sessionId = UUID.randomUUID();

    when(jwtService.extractUserId("faketoken")).thenReturn(userId);
    when(jwtService.extractSessionIdAllowExpired("faketoken")).thenReturn(sessionId);

    DevicesResponseDto response =
        new DevicesResponseDto(
            List.of(
                new DeviceEntryDto(
                    UUID.randomUUID(), "Linux", "Firefox", "Earth", DeviceStatus.CURRENT_SESSION)));

    when(getSessionInfoUseCase.handle(any()))
        .thenReturn(new GetSessionInfoUseCasePort.Result(response));

    mockMvc
        .perform(get("/api/user/get-devices").header("Authorization", AUTH_HEADER))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.devices").isArray());
  }

  @Test
  void revokeSession_returnsNoContent() throws Exception {
    UUID userId = UUID.randomUUID();
    UUID currentSessionId = UUID.randomUUID();
    UUID revokeSessionId = UUID.randomUUID();

    when(jwtService.extractUserId("faketoken")).thenReturn(userId);
    when(jwtService.extractSessionIdAllowExpired("faketoken")).thenReturn(currentSessionId);

    mockMvc
        .perform(
            delete("/api/user/revoke-session/{id}", revokeSessionId)
                .header("Authorization", AUTH_HEADER))
        .andExpect(status().isNoContent());
  }

  @Test
  void deleteAccount_returnsNoContent_andClearsCookie() throws Exception {
    UUID userId = UUID.randomUUID();
    when(jwtService.extractUserId("faketoken")).thenReturn(userId);

    mockMvc
        .perform(delete("/api/user/delete-account").header("Authorization", AUTH_HEADER))
        .andExpect(status().isNoContent())
        .andExpect(cookie().maxAge("refreshToken", 0));
  }
}
