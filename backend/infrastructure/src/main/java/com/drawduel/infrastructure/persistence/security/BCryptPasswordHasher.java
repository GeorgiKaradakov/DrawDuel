package com.drawduel.infrastructure.persistence.security;

import com.drawduel.domain.ports.PasswordHasher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordHasher implements PasswordHasher {
  private final BCryptPasswordEncoder encoder;

  public BCryptPasswordHasher(BCryptPasswordEncoder encoder) {
    this.encoder = encoder;
  }

  @Override
  public String hash(String rawPass) {
    return encoder.encode(rawPass);
  }
}
