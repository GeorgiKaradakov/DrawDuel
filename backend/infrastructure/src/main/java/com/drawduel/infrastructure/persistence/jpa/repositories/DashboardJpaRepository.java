package com.drawduel.infrastructure.persistence.jpa.repositories;

import com.drawduel.domain.models.LeaderboardEntry;
import com.drawduel.domain.models.RecentMatches;
import com.drawduel.infrastructure.persistence.jpa.entities.JpaGameEntity;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface DashboardJpaRepository extends JpaRepository<JpaGameEntity, UUID> {

  @Query(
      """
        SELECT COUNT(g)
        FROM JpaGameEntity g
        WHERE g.playerAId = :userId OR g.playerBId = :userId
      """)
  int countTotalMatches(@Param("userId") UUID userId);

  @Query(
      """
        SELECT COUNT(g)
        FROM JpaGameEntity g
        WHERE g.winnerId = :userId
      """)
  int countWins(@Param("userId") UUID userId);

  @Query(
"""
  SELECT COUNT(g)
  FROM JpaGameEntity g
  WHERE (g.playerAId = :userId OR g.playerBId = :userId)
    AND g.winnerId IS NULL
""")
  int countDraws(@Param("userId") UUID userId);

  @Query(
      """
        SELECT new com.drawduel.domain.models.RecentMatches(
          g.id,
          CASE WHEN g.playerAId = :userId THEN g.playerADrawPoints ELSE g.playerBDrawPoints END,
          CASE WHEN g.playerAId = :userId THEN g.playerAGuessPoints ELSE g.playerBGuessPoints END,
          CASE
            WHEN g.winnerId = :userId THEN 'Win'
            WHEN g.winnerId IS NULL THEN 'Draw'
            ELSE 'Lose'
          END
        )
        FROM JpaGameEntity g
        WHERE g.playerAId = :userId OR g.playerBId = :userId
        ORDER BY g.endedAt DESC
      """)
  List<RecentMatches> findRecentMatches(@Param("userId") UUID userId, Pageable pageable);

  @Query(
      """
        SELECT new com.drawduel.domain.models.LeaderboardEntry(
          0,
          u.id,
          u.username,
          SUM(
            CASE
              WHEN g.playerAId = u.id THEN g.playerADrawPoints + g.playerAGuessPoints
              ELSE g.playerBDrawPoints + g.playerBGuessPoints
            END
          ),
          COUNT(CASE WHEN g.winnerId = u.id THEN 1 END)
        )
        FROM JpaGameEntity g
        JOIN JpaUserEntity u ON u.id = g.playerAId OR u.id = g.playerBId
        GROUP BY u.id, u.username
        ORDER BY 4 DESC
      """)
  List<LeaderboardEntry> findLeaderboard(Pageable pageable);

  @Query(
"""
SELECT g.endedAt, COUNT(g)
FROM JpaGameEntity g
WHERE g.playerAId = :userId OR g.playerBId = :userId
GROUP BY g.endedAt
""")
  List<Object[]> countGamesPerDayRaw(@Param("userId") UUID userId);
}
