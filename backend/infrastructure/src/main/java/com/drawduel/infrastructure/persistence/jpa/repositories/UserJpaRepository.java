package com.drawduel.infrastructure.persistence.jpa.repositories;

import com.drawduel.infrastructure.persistence.jpa.entities.JpaUserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<JpaUserEntity, UUID> {
  Optional<JpaUserEntity> findById(UUID id);

  Optional<JpaUserEntity> findByEmail(String email);

  Optional<JpaUserEntity> findByUsername(String username);
}
