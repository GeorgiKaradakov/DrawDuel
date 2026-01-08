package com.drawduel.application.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DashboardResponseDto {
  public DashboardStatsDto stats;
  public List<RecentMatchDto> recentMatches;
  public List<LeaderboardEntryDto> leaderboard;
  public List<GamesPerDayDto> chartData;
}
