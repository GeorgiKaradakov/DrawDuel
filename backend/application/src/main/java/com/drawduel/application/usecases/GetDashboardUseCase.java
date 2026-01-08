package com.drawduel.application.usecases;

import com.drawduel.application.dtos.*;
import com.drawduel.application.ports.GetDashboardUseCasePort;
import com.drawduel.domain.models.*;
import com.drawduel.domain.ports.DashboardRepository;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.util.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetDashboardUseCase implements GetDashboardUseCasePort {

  private final DashboardRepository repository;

  @Override
  public Result handle(Query q) {
    UUID userId = q.userId();

    try {
      int totalMatches = repository.countTotalMatches(userId);
      int wins = repository.countWins(userId);
      int draws = repository.countDraws(userId);
      int losses = totalMatches - wins - draws;
      double ratio = totalMatches == 0 ? 0 : (double) wins / totalMatches;

      DashboardStats stats = new DashboardStats(totalMatches, wins, losses, ratio);

      List<RecentMatches> recentMatches =
          Optional.ofNullable(repository.findRecentMatches(userId, 20)).orElse(List.of());

      List<LeaderboardEntry> top10 =
          Optional.ofNullable(repository.findTopLeaderboard(10)).orElse(List.of());

      Optional<LeaderboardEntry> userRank = repository.findUserLeaderboard(userId);

      List<LeaderboardEntry> leaderboard = new ArrayList<>();

      int rank = 1;

      for (LeaderboardEntry entry : top10) {
        entry.setRank(rank++);
        leaderboard.add(entry);
      }

      if (userRank.isPresent()) {
        boolean exists =
            top10.stream().anyMatch(e -> e.getUserId().equals(userRank.get().getUserId()));

        if (!exists) {
          leaderboard.add(userRank.get());
          System.out.println("➕ [Dashboard] User rank appended");
        }
      }

      List<Object[]> raw =
          Optional.ofNullable(repository.countGamesPerDayRaw(userId)).orElse(List.of());

      Map<String, Integer> dayCounts = new LinkedHashMap<>();

      for (Object[] row : raw) {
        if (row[0] == null) continue;

        Instant endedAtInstant = (Instant) row[0];
        OffsetDateTime endedAt = endedAtInstant.atOffset(ZoneOffset.UTC);

        int count = ((Long) row[1]).intValue();

        String day = endedAt.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

        dayCounts.merge(day, count, Integer::sum);
      }

      List<GamesPerDayDto> chart =
          dayCounts.entrySet().stream()
              .map(e -> new GamesPerDayDto(e.getKey(), e.getValue()))
              .toList();

      DashboardResponseDto response =
          new DashboardResponseDto(
              mapStats(stats),
              recentMatches.stream().map(this::mapMatch).toList(),
              leaderboard.stream().map(this::mapLeaderboard).toList(),
              chart);

      return new Result(response);

    } catch (Exception e) {
      System.out.println("Dashboard Failed for userId = " + userId);
      e.printStackTrace();
      throw e;
    }
  }

  private DashboardStatsDto mapStats(DashboardStats s) {
    return new DashboardStatsDto(
        s.getTotalMatches(), s.getTotalWins(), s.getTotalLosses(), s.getWinLossRatio());
  }

  private RecentMatchDto mapMatch(RecentMatches m) {
    return new RecentMatchDto(
        m.getMatchId(), m.getDrawPoints(), m.getGuessPoints(), m.getOutcome());
  }

  private LeaderboardEntryDto mapLeaderboard(LeaderboardEntry e) {
    return new LeaderboardEntryDto(
        e.getRank(), e.getUserId(), e.getUserName(), e.getScore(), e.getWins());
  }
}
