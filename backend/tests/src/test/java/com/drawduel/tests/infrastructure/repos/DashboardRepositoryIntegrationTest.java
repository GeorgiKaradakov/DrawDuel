package com.drawduel.tests.infrastructure.repos;

import static org.assertj.core.api.Assertions.assertThat;

import com.drawduel.application.dtos.RegisterRequestDto;
import com.drawduel.application.services.JwtService;
import com.drawduel.application.usecases.RegisterUseCase;
import com.drawduel.domain.enums.GameStatus;
import com.drawduel.domain.models.LeaderboardEntry;
import com.drawduel.domain.models.RecentMatches;
import com.drawduel.domain.ports.DashboardRepository;
import com.drawduel.infrastructure.persistence.jpa.entities.JpaGameEntity;
import com.drawduel.tests.BaseIntegrationTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Transactional
class DashboardRepositoryIntegrationTest extends BaseIntegrationTest {

  @Autowired private DashboardRepository dashboardRepository;
  @Autowired private RegisterUseCase registerUseCase;
  @Autowired private JwtService jwtService;
  @PersistenceContext private EntityManager entityManager;

  @Test
  void countsMatchesWinsAndDrawsCorrectly() {
    UUID user = UUID.randomUUID();
    UUID other = UUID.randomUUID();

    saveGame(user, other, user);
    saveGame(user, other, null);
    saveGame(user, other, other);

    assertThat(dashboardRepository.countTotalMatches(user)).isEqualTo(3);
    assertThat(dashboardRepository.countWins(user)).isEqualTo(1);
    assertThat(dashboardRepository.countDraws(user)).isEqualTo(1);
  }

  @Test
  void returnsRecentMatchesWithLimit() {
    UUID user = UUID.randomUUID();
    UUID other = UUID.randomUUID();

    for (int i = 0; i < 5; i++) {
      saveGame(user, other, user);
    }

    List<RecentMatches> recent = dashboardRepository.findRecentMatches(user, 3);

    assertThat(recent).hasSize(3);

    Long dbCount =
        entityManager
            .createQuery(
                "SELECT COUNT(g) FROM JpaGameEntity g WHERE g.playerAId = :user", Long.class)
            .setParameter("user", user)
            .getSingleResult();

    assertThat(dbCount).isEqualTo(5);
  }

  @Test
  void leaderboardIsEmptyWhenNoGamesExist() {
    List<LeaderboardEntry> leaderboard = dashboardRepository.findTopLeaderboard(10);

    assertThat(leaderboard).isEmpty();
  }

  @Test
  void leaderboardRespectsLimit() {
    UUID a = registerUser("a");
    UUID b = registerUser("b");
    UUID c = registerUser("c");

    saveGame(a, b, a);
    saveGame(b, c, b);
    saveGame(c, a, c);

    List<LeaderboardEntry> leaderboard = dashboardRepository.findTopLeaderboard(2);

    assertThat(leaderboard).hasSize(2);
  }

  @Test
  void findUserLeaderboardReturnsCorrectEntry() {
    UUID userId = registerUser("alice");
    UUID other = registerUser("bob");

    saveGame(userId, other, userId);
    saveGame(userId, other, userId);

    Optional<LeaderboardEntry> entry = dashboardRepository.findUserLeaderboard(userId);

    assertThat(entry).isPresent();
    assertThat(entry.get().getUserId()).isEqualTo(userId);
    assertThat(entry.get().getScore()).isGreaterThan(0);
  }

  // ---------- helpers ----------

  private void saveGame(UUID a, UUID b, UUID winner) {
    JpaGameEntity g = new JpaGameEntity();
    g.setId(UUID.randomUUID());
    g.setPlayerAId(a);
    g.setPlayerBId(b);
    g.setStatus(GameStatus.FINISHED);
    g.setWinnerId(winner);
    g.setPlayerADrawPoints(10);
    g.setPlayerAGuessPoints(0);
    g.setPlayerBDrawPoints(5);
    g.setPlayerBGuessPoints(0);
    g.setStartedAt(Instant.now().minusSeconds(300));
    g.setEndedAt(Instant.now());

    entityManager.persist(g);
    entityManager.flush();
  }

  private UUID registerUser(String username) {
    RegisterRequestDto dto =
        new RegisterRequestDto(
            username,
            username + "@test.com",
            "password123",
            "password123",
            null,
            "127.0.0.1",
            "JUnit",
            "NL");

    var result = registerUseCase.handle(new RegisterUseCase.Query(dto));
    return jwtService.extractUserId(result.response().getAccessToken());
  }
}
