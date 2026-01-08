package com.drawduel.domain.models;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RecentMatches {
  public UUID matchId;
  public int drawPoints;
  public int guessPoints;
  public String outcome;
}
