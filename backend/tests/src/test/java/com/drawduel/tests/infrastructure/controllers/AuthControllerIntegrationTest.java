package com.drawduel.tests.infrastructure.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.drawduel.application.dtos.*;
import com.drawduel.application.ports.*;
import com.drawduel.application.services.TokensService;
import com.drawduel.application.usecases.LoginUseCase;
import com.drawduel.tests.BaseIntegrationTest;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class AuthControllerTest extends BaseIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private RegisterUseCasePort registerUseCase;
  @MockBean private LoginUseCasePort loginUseCase;
  @MockBean private GetUserByIdUseCasePort getUserByIdUseCase;
  @MockBean private RevokeRefreshTokenUseCasePort revokeRefreshTokenUseCase;
  @MockBean private GetRefreshTokenUseCasePort getRefreshTokenUseCase;
  @MockBean private TokensService tokensService;

  private final String ACCESS_TOKEN = "access123";
  private final String REFRESH_TOKEN = "refresh456";

  private RegisterRequestDto registerReq;
  private LoginRequestDto loginReq;

  @BeforeEach
  void setup() {
    registerReq = new RegisterRequestDto();
    registerReq.setEmail("test@example.com");
    registerReq.setUsername("testuser");
    registerReq.setPass("123456");
    registerReq.setRepeatPass("123456");

    loginReq = new LoginRequestDto();
    loginReq.setIdentifier("testuser");
    loginReq.setPassword("123456");
  }

  @Test
  void shouldRegisterSuccessfully() throws Exception {
    var authResponse = new AuthResponseDto(ACCESS_TOKEN, REFRESH_TOKEN);
    var result = new RegisterUseCasePort.Result(authResponse);
    when(registerUseCase.handle(any())).thenReturn(result);

    mockMvc
        .perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "email": "test@example.com",
                      "username": "testuser",
                      "pass": "123456",
                      "repeatPass": "123456"
                    }
                    """)
                .header("User-Agent", "JUnit"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value(ACCESS_TOKEN))
        .andExpect(header().exists("Set-Cookie"));
  }

  @Test
  void shouldLoginSuccessfully() throws Exception {
    var result = new LoginUseCase.Result(ACCESS_TOKEN, REFRESH_TOKEN);
    when(loginUseCase.handle(any())).thenReturn(result);

    mockMvc
        .perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "identifier": "testuser",
                      "password": "123456"
                    }
                    """)
                .header("User-Agent", "JUnit"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value(ACCESS_TOKEN))
        .andExpect(header().exists("Set-Cookie"));
  }

  @Test
  void shouldRefreshTokensSuccessfully() throws Exception {
    UserSessionDto session =
        new UserSessionDto(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "127.0.0.1",
            "JUnit",
            "unknown",
            "refreshToken123",
            false,
            Instant.now(),
            Instant.now().plusSeconds(3600));

    var user = new UserDto(session.getUserId(), "testuser", "test@example.com", Instant.now());

    when(getRefreshTokenUseCase.handle(any()))
        .thenReturn(new GetRefreshTokenUseCasePort.Result(session));
    when(getUserByIdUseCase.handle(any())).thenReturn(new GetUserByIdUseCasePort.Result(user));
    when(tokensService.generateTokens(any(UserDto.class), any(), any(), any()))
        .thenReturn(new String[] {ACCESS_TOKEN, REFRESH_TOKEN});

    mockMvc
        .perform(
            post("/api/auth/refresh-tokens")
                .cookie(new jakarta.servlet.http.Cookie("refreshToken", "oldToken")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value(ACCESS_TOKEN))
        .andExpect(header().exists("Set-Cookie"));
  }

  @Test
  void shouldReturnUnauthorizedWhenNoCookieOnRefresh() throws Exception {
    mockMvc.perform(post("/api/auth/refresh-tokens")).andExpect(status().isUnauthorized());
  }

  @Test
  void shouldReturnUnauthorizedWhenRefreshTokenInvalid() throws Exception {
    when(getRefreshTokenUseCase.handle(any()))
        .thenThrow(new IllegalArgumentException("Invalid token"));
    mockMvc
        .perform(
            post("/api/auth/refresh-tokens")
                .cookie(new jakarta.servlet.http.Cookie("refreshToken", "badToken")))
        .andExpect(status().isUnauthorized());
  }
}
