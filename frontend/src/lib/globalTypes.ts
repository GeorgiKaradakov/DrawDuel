export interface GuessEntry {
  playerName: string;
  guess: string;
  correct: boolean;
}

export interface RoundResults {
  drawerName: string;
  guesserName: string;
  drawerScore: number;
  guesserScore: number;
}

export interface PresentWinnerProps {
  playerAName: string;
  playerBName: string;
  playerAScore: number;
  playerBScore: number;
  playerId: string;
  winner: string;
}

export interface DashboardStats {
  totalMatches: number;
  totalWins: number;
  totalLosses: number;
  winLossRatio: number;
}

export interface RecentMatch {
  matchId: string;
  drawPoints: number;
  guessPoints: number;
  outcome: "Win" | "Lose" | "Draw";
}

export interface LeaderboardEntry {
  rank: number;
  username: string;
  score: number;
  wins: number;
}

export interface ChartPoint {
  day: string;
  games: number;
}

export interface DashboardResponse {
  stats: DashboardStats;
  recentMatches: RecentMatch[];
  leaderboard: LeaderboardEntry[];
  chartData: ChartPoint[];
}
