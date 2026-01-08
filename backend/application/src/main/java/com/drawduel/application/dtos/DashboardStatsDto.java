package com.drawduel.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class DashboardStatsDto {

  public int totalMatches;
  public int totalWins;
  public int totalLosses;
  public double winLossRatio;
}
