package com.drawduel.domain.ports;

import com.drawduel.domain.models.LeaderboardEntry;
import com.drawduel.domain.models.RecentMatches;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DashboardRepository {
  int countTotalMatches(UUID userId);

  int countWins(UUID userId);

  int countDraws(UUID userId);

  List<RecentMatches> findRecentMatches(UUID userId, int limit);

  List<LeaderboardEntry> findTopLeaderboard(int limit);

  Optional<LeaderboardEntry> findUserLeaderboard(UUID userId);

  List<Object[]> countGamesPerDayRaw(UUID userId);
}
