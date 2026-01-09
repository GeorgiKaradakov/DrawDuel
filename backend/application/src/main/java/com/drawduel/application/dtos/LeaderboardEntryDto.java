package com.drawduel.application.dtos;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class LeaderboardEntryDto {
  public int rank;
  public UUID userId;
  public String userName;
  public Long score;
  public Long wins;
}
