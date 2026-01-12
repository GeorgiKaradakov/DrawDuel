package com.drawduel.domain.models;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LeaderboardEntry {

  public int Rank;
  public UUID userId;
  public String userName;
  public Long Score;
  public Long wins;
}
