package com.drawduel.domain.models;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Round {
  private UUID Id;
  private UUID drawerId;
  private UUID guesserId;
  private int guessCount;
  private String chosenWord;
  private int roundNumber;
  private boolean completed = false;

  public Round(UUID id, UUID drawerId, UUID guesserId, int roundNumber) {
    this.Id = id;
    this.drawerId = drawerId;
    this.guesserId = guesserId;
    this.roundNumber = roundNumber;
    this.guessCount = 0;
  }

  public void incrementGuessCount() {
    this.guessCount += 1;
  }
}
