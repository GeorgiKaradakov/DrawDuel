package com.drawduel.domain.models;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LeaderboardEntry {

  public int Rank;
  public UUID userId;
  public String userName;
  public Long Score;
  public Long wins;
}
