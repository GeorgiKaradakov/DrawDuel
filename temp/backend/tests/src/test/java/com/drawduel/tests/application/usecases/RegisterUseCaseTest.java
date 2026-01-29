package com.drawduel.tests.application.usecases;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.drawduel.application.dtos.RegisterRequestDto;
import com.drawduel.application.ports.ImageStorageSevicePort;
import com.drawduel.application.ports.RegisterUseCasePort;
import com.drawduel.application.services.TokensService;
import com.drawduel.application.usecases.RegisterUseCase;
import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.PasswordHasher;
import com.drawduel.domain.ports.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegisterUseCaseTest {

  private UserRepository userRepo;
  private PasswordHasher hasher;
  private TokensService tokensService;
  private RegisterUseCasePort useCase;
  private ImageStorageSevicePort imageStorage;

  @BeforeEach
  void setup() {
    userRepo = mock(UserRepository.class);
    hasher = mock(PasswordHasher.class);
    tokensService = mock(TokensService.class);
    imageStorage = mock(ImageStorageSevicePort.class);

    useCase =
        new RegisterUseCase(userRepo, hasher, tokensService, imageStorage, "default-image-url");
  }

  @Test
  void shouldRegisterUserSuccessfully() {
    RegisterRequestDto req =
        new RegisterRequestDto(
            "guts@drawduel.com", "guts", "123", "123", "127.0.0.1", "Chrome", "Earth", "Linux");

    when(userRepo.findByEmail(req.getEmail())).thenReturn(Optional.empty());
    when(userRepo.findByUsername(req.getUsername())).thenReturn(Optional.empty());
    when(hasher.hash("123")).thenReturn("hashed123");

    when(tokensService.generateTokens(
            any(User.class), eq("127.0.0.1"), eq("Chrome"), eq("Earth"), eq("Linux")))
        .thenReturn(new String[] {"access", "refresh"});

    var result = useCase.handle(new RegisterUseCasePort.Query(req));

    assertThat(result.response().getAccessToken()).isEqualTo("access");
    verify(userRepo).save(any(User.class));
  }
}
