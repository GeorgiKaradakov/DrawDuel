package com.drawduel.infrastructure.persistence.jpa.entities;

import com.drawduel.domain.enums.GameStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
public class JpaGameEntity {

  @Id private UUID id;

  @Column(nullable = false)
  private UUID playerAId;

  @Column(nullable = false)
  private UUID playerBId;

  @Column(nullable = false)
  private int totalRounds = 4;

  @Column(nullable = false)
  private int currentRound = 1;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private GameStatus status = GameStatus.WAITING; // IN_PROGRESS | FINISHED

  private UUID winnerId;

  @Column(nullable = false)
  private int playerADrawPoints = 0;

  @Column(nullable = false)
  private int playerAGuessPoints = 0;

  @Column(nullable = false)
  private int playerBDrawPoints = 0;

  @Column(nullable = false)
  private int playerBGuessPoints = 0;

  @Column(nullable = false, updatable = false)
  private Instant startedAt = Instant.now();

  private Instant endedAt;

  public void addScores(int drawerPoints, int guesserPoints, boolean drawerIsPlayerA) {
    if (drawerIsPlayerA) {
      this.playerADrawPoints += drawerPoints;
      this.playerBGuessPoints += guesserPoints;
    } else {
      this.playerBDrawPoints += drawerPoints;
      this.playerAGuessPoints += guesserPoints;
    }
  }

  public void finish() {
    this.status = GameStatus.FINISHED;
    this.endedAt = Instant.now();
  }
}
