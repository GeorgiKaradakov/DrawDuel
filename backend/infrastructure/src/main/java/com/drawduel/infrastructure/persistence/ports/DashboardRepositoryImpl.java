package com.drawduel.infrastructure.persistence.ports;

import com.drawduel.domain.models.LeaderboardEntry;
import com.drawduel.domain.models.RecentMatches;
import com.drawduel.domain.ports.DashboardRepository;
import com.drawduel.infrastructure.persistence.jpa.repositories.DashboardJpaRepository;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

@RequiredArgsConstructor
public class DashboardRepositoryImpl implements DashboardRepository {

  private final DashboardJpaRepository jpa;

  @Override
  public int countTotalMatches(UUID userId) {
    return jpa.countTotalMatches(userId);
  }

  @Override
  public int countWins(UUID userId) {
    return jpa.countWins(userId);
  }

  @Override
  public int countDraws(UUID userId) {
    return jpa.countDraws(userId);
  }

  @Override
  public List<RecentMatches> findRecentMatches(UUID userId, int limit) {
    return jpa.findRecentMatches(userId, PageRequest.of(0, limit));
  }

  @Override
  public List<LeaderboardEntry> findTopLeaderboard(int limit) {
    return jpa.findLeaderboard(PageRequest.of(0, limit));
  }

  @Override
  public Optional<LeaderboardEntry> findUserLeaderboard(UUID userId) {
    return findTopLeaderboard(Integer.MAX_VALUE).stream()
        .filter(e -> e.getUserName().equals(userId.toString())) // adapt if needed
        .findFirst();
  }

  @Override
  public List<Object[]> countGamesPerDayRaw(UUID userId) {
    return jpa.countGamesPerDayRaw(userId);
  }
}
