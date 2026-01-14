package com.drawduel.tests.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.drawduel.domain.models.User;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class UserTest {
  @Test
  void shouldCreateUserWithGivenValues() {
    Instant now = Instant.now();
    UUID id = UUID.randomUUID();
    User user = new User(id, "testuser", "test@test.com", "hash1234", now, null);

    assertThat(user.getUsername()).isEqualTo("testuser");
    assertThat(user.getEmail()).isEqualTo("test@test.com");
    assertThat(user.getPassHash()).isEqualTo("hash1234");
    assertThat(user.getCreatedAt()).isEqualTo(now);
    assertThat(user.getId()).isEqualTo(id);
    assertThat(user.getProfileImageUrl()).isEqualTo(null);
  }
}
