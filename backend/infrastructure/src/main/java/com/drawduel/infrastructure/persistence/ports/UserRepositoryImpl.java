package com.drawduel.infrastructure.persistence.ports;

import com.drawduel.domain.models.User;
import com.drawduel.domain.ports.UserRepository;
import com.drawduel.infrastructure.persistence.jpa.entities.JpaUserEntity;
import com.drawduel.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {
  private final UserJpaRepository userJpaRepository;

  public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
    this.userJpaRepository = userJpaRepository;
  }

  @Override
  public void save(User user) {
    JpaUserEntity entity = new JpaUserEntity();
    entity.setId(user.getId());
    entity.setUsername(user.getUsername());
    entity.setEmail(user.getEmail());
    entity.setPasswordHash(user.getPassHash());
    entity.setCreatedAt(user.getCreatedAt());
    userJpaRepository.save(entity);
  }

  @Override
  public Optional<User> findById(UUID id) {
    return userJpaRepository.findById(id).map(this::ToDomain);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return userJpaRepository.findByUsername(username).map(this::ToDomain);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userJpaRepository.findByEmail(email).map(this::ToDomain);
  }

  @Override
  public void deleteById(UUID id) {
    userJpaRepository.deleteById(id);
  }

  private User ToDomain(JpaUserEntity entity) {
    return new User(
        entity.getId(),
        entity.getUsername(),
        entity.getEmail(),
        entity.getPasswordHash(),
        entity.getCreatedAt());
  }
}
