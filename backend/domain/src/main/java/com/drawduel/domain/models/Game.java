package com.drawduel.domain.models;

import com.drawduel.domain.enums.GameStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Game {

  private UUID id;

  private UUID playerAId;

  private UUID playerBId;

  private int totalRounds = 4;

  private int currentRound = 1;

  private GameStatus status = GameStatus.WAITING; // IN_PROGRESS | FINISHED

  private UUID winnerId;

  private int playerADrawPoints = 0;

  private int playerAGuessPoints = 0;

  private int playerBDrawPoints = 0;

  private int playerBGuessPoints = 0;

  private Instant startedAt = Instant.now();

  private Instant endedAt;

  public int getTotalScoreA() {
    return playerADrawPoints + playerAGuessPoints;
  }

  public int getTotalScoreB() {
    return playerBDrawPoints + playerBGuessPoints;
  }

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
