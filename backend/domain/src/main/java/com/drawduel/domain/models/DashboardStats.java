package com.drawduel.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DashboardStats {
  public int totalMatches;
  public int totalWins;
  public int totalLosses;
  public Double winLossRatio;
}
