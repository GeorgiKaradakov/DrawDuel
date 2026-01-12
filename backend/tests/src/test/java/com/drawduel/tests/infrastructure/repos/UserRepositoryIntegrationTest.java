package com.drawduel.tests.infrastructure.repos;

import static org.assertj.core.api.Assertions.assertThat;

import com.drawduel.domain.models.User;
import com.drawduel.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import com.drawduel.infrastructure.persistence.ports.UserRepositoryImpl;
import com.drawduel.tests.BaseIntegrationTest;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserRepositoryIntegrationTest extends BaseIntegrationTest {

  @Autowired private UserJpaRepository userJpaRepository;

  private UserRepositoryImpl userRepository;

  @BeforeEach
  void setup() {
    userRepository = new UserRepositoryImpl(userJpaRepository);
  }

  @Test
  void shouldSaveAndRetrieveUserById() {
    User user = new User(UUID.randomUUID(), "guts", "guts@drawduel.com", "hash123", Instant.now());

    userRepository.save(user);

    var found = userRepository.findById(user.getId());
    assertThat(found).isPresent();
    assertThat(found.get().getUsername()).isEqualTo("guts");
    assertThat(found.get().getEmail()).isEqualTo("guts@drawduel.com");
    assertThat(found.get().getPassHash()).isEqualTo("hash123");
  }

  @Test
  void shouldFindUserByUsernameAndEmail() {
    User user =
        new User(UUID.randomUUID(), "griffith", "griffith@falcons.com", "hash", Instant.now());
    userRepository.save(user);

    var byUsername = userRepository.findByUsername("griffith");
    var byEmail = userRepository.findByEmail("griffith@falcons.com");

    assertThat(byUsername).isPresent();
    assertThat(byEmail).isPresent();
  }

  @Test
  void shouldReturnEmptyWhenUserNotFound() {
    assertThat(userRepository.findByUsername("none")).isEmpty();
    assertThat(userRepository.findByEmail("none@mail.com")).isEmpty();
    assertThat(userRepository.findById(UUID.randomUUID())).isEmpty();
  }
}
