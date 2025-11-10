package com.drawduel.domain.ports;

public interface PasswordHasher {
  String hash(String rawPass);

  Boolean matches(String rawPass, String hashedPass);
}
