package com.drawduel.domain.ports;

import com.drawduel.domain.models.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
  void save(User user);

  void deleteById(UUID id);

  Optional<User> findById(UUID id);

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);
}
