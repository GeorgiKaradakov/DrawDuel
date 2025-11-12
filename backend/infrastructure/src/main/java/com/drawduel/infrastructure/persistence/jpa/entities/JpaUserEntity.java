package com.drawduel.infrastructure.persistence.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class JpaUserEntity {
  @Id
  @Column(columnDefinition = "uuid")
  private UUID id;

  @Column(nullable = false, unique = true, length = 50)
  private String username;

  @Column(nullable = false, unique = true, length = 255)
  private String email;

  @Column(name = "password_hash", nullable = false, length = 255)
  private String passwordHash;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonIgnore
  private List<JpaTokensEntity> tokens = new ArrayList<>();
}
