package com.drawduel.infrastructure.persistence.adapters;

import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.UserRepository;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepository implements UserRepository {
  private final Map<UUID, User> byId = new ConcurrentHashMap<>();
  private final Map<String, UUID> byUsername = new ConcurrentHashMap<>();
  private final Map<String, UUID> byEmail = new ConcurrentHashMap<>();

  @Override
  public void save(User user) {
    byId.put(user.getId(), user);
    byUsername.put(user.getUsername().toLowerCase(Locale.ROOT), user.getId());
    byEmail.put(user.getEmail().toLowerCase(Locale.ROOT), user.getId());
  }

  @Override
  public Optional<User> findById(UUID id) {
    return Optional.ofNullable(byId.get(id));
  }

  @Override
  public Optional<User> findByUsername(String username) {
    if (username == null) return Optional.empty();
    UUID id = byUsername.get(username.toLowerCase(Locale.ROOT));
    return Optional.ofNullable(id).map(byId::get);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    if (email == null) return Optional.empty();
    UUID id = byEmail.get(email.toLowerCase(Locale.ROOT));
    return Optional.ofNullable(id).map(byId::get);
  }
}
