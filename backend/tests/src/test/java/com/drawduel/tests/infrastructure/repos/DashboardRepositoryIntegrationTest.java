package com.drawduel.tests.infrastructure.repos;

import static org.assertj.core.api.Assertions.assertThat;

import com.drawduel.application.dtos.RegisterRequestDto;
import com.drawduel.application.services.JwtService;
import com.drawduel.application.usecases.RegisterUseCase;
import com.drawduel.domain.enums.GameStatus;
import com.drawduel.domain.models.LeaderboardEntry;
import com.drawduel.domain.ports.DashboardRepository;
import com.drawduel.infrastructure.persistence.jpa.entities.JpaGameEntity;
import com.drawduel.infrastructure.persistence.jpa.repositories.GameJpaRepository;
import com.drawduel.tests.BaseIntegrationTest;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class DashboardRepositoryIntegrationTest extends BaseIntegrationTest {

  @Autowired DashboardRepository dashboardRepository;

  @Autowired GameJpaRepository gameJpaRepository;

  @Autowired RegisterUseCase registerUseCase;

  @Autowired JwtService jwtService;

  @Test
  void countsMatchesWinsAndDrawsCorrectly() {
    UUID user = UUID.randomUUID();
    UUID other = UUID.randomUUID();

    saveGame(user, other, user, 10, 5);
    saveGame(user, other, null, 10, 10);
    saveGame(user, other, other, 5, 10);

    assertThat(dashboardRepository.countTotalMatches(user)).isEqualTo(3);
    assertThat(dashboardRepository.countWins(user)).isEqualTo(1);
    assertThat(dashboardRepository.countDraws(user)).isEqualTo(1);
  }

  @Test
  void returnsRecentMatches() {
    UUID user = UUID.randomUUID();
    UUID other = UUID.randomUUID();

    for (int i = 0; i < 5; i++) {
      saveGame(user, other, user, 10 + i, 5);
    }

    var recent = dashboardRepository.findRecentMatches(user, 3);

    assertThat(recent).hasSize(3);
  }

  @Test
  void buildsLeaderboardCorrectly() {
    UUID a = registerUser("alice");
    UUID b = registerUser("bob");
    UUID c = registerUser("charlie");

    saveGame(a, b, a, 20, 10);
    saveGame(a, c, a, 30, 5);
    saveGame(b, c, b, 25, 5);

    List<LeaderboardEntry> leaderboard = dashboardRepository.findTopLeaderboard(10);

    assertThat(leaderboard).isNotEmpty();
    assertThat(leaderboard.get(0).getScore()).isGreaterThan(0);
    assertThat(leaderboard.get(0).getUserName()).isNotNull();
  }

  @Test
  void countsGamesPerDayRaw() {
    UUID user = UUID.randomUUID();
    UUID other = UUID.randomUUID();

    saveGame(user, other, user, 10, 5);
    saveGame(user, other, user, 12, 3);

    List<Object[]> raw = dashboardRepository.countGamesPerDayRaw(user);

    assertThat(raw).isNotEmpty();
    assertThat(raw.get(0)[0]).isInstanceOf(Instant.class);
    assertThat(raw.get(0)[1]).isInstanceOf(Long.class);
  }

  private void saveGame(UUID a, UUID b, UUID winner, int scoreA, int scoreB) {

    JpaGameEntity g = new JpaGameEntity();
    g.setId(UUID.randomUUID());
    g.setPlayerAId(a);
    g.setPlayerBId(b);
    g.setStatus(GameStatus.FINISHED);
    g.setWinnerId(winner);
    g.setPlayerADrawPoints(scoreA);
    g.setPlayerAGuessPoints(0);
    g.setPlayerBDrawPoints(scoreB);
    g.setPlayerBGuessPoints(0);
    g.setStartedAt(Instant.now().minusSeconds(300));
    g.setEndedAt(Instant.now());

    gameJpaRepository.save(g);
  }

  private UUID registerUser(String username) {

    RegisterRequestDto dto =
        new RegisterRequestDto(
            username,
            username + "@test.com",
            "password123",
            "password123",
            "127.0.0.1",
            "JUnit",
            "NL");

    var result = registerUseCase.handle(new RegisterUseCase.Query(dto));

    // Extract userId from JWT
    String accessToken = result.response().getAccessToken();

    return jwtService.extractUserId(accessToken);
  }
}
