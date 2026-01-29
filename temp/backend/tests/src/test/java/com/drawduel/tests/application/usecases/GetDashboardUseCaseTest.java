package com.drawduel.tests.application.usecases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import com.drawduel.application.ports.GetDashboardUseCasePort;
import com.drawduel.application.usecases.GetDashboardUseCase;
import com.drawduel.domain.models.LeaderboardEntry;
import com.drawduel.domain.models.RecentMatches;
import com.drawduel.domain.ports.DashboardRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetDashboardUseCaseTest {

  private DashboardRepository repository;
  private GetDashboardUseCasePort useCase;
  private UUID userId;

  @BeforeEach
  void setUp() {
    repository = mock(DashboardRepository.class);
    useCase = new GetDashboardUseCase(repository);
    userId = UUID.randomUUID();
  }

  @Test
  void should_build_dashboard_successfully() {
    // stats
    when(repository.countTotalMatches(userId)).thenReturn(10);
    when(repository.countWins(userId)).thenReturn(4);
    when(repository.countDraws(userId)).thenReturn(2);

    // recent matches
    when(repository.findRecentMatches(userId, 20))
        .thenReturn(List.of(new RecentMatches(UUID.randomUUID(), 10, 5, "Win")));

    // leaderboard
    when(repository.findTopLeaderboard(10))
        .thenReturn(List.of(new LeaderboardEntry(0, UUID.randomUUID(), "alice", 120L, 3L)));

    when(repository.findUserLeaderboard(userId)).thenReturn(Optional.of(new LeaderboardEntry()));

    // chart raw data
    when(repository.countGamesPerDayRaw(userId))
        .thenReturn(
            List.of(
                new Object[] {Instant.now(), 3L},
                new Object[] {Instant.now().minusSeconds(86400), 2L}));

    // act
    var result = useCase.handle(new GetDashboardUseCasePort.Query(userId));

    // assert
    var response = result.dashboardResponse();

    assertEquals(10, response.getStats().getTotalMatches());
    assertEquals(4, response.getStats().getTotalWins());
    assertEquals(4, response.getStats().getTotalLosses());
    assertEquals(2, response.getLeaderboard().size());
    assertFalse(response.getChartData().isEmpty());
  }
}
