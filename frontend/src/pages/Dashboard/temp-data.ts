export const MatchesColumns = [
  { key: "matchId", label: "Match ID" },
  { key: "drawPoints", label: "Draw Points" },
  { key: "guessPoints", label: "Guess Points" },
  { key: "outcome", label: "Outcome" },
];

export const Matches = [
  { matchId: "a1b2c3d4", drawPoints: 120, guessPoints: 40, outcome: "Win" },
  { matchId: "e5f6g7h8", drawPoints: 90, guessPoints: 60, outcome: "Lose" },
  { matchId: "i9j0k1l2", drawPoints: 200, guessPoints: 80, outcome: "Win" },
  { matchId: "m3n4o5p6", drawPoints: 150, guessPoints: 45, outcome: "Win" },
  { matchId: "q7r8s9t0", drawPoints: 100, guessPoints: 50, outcome: "Lose" },
  { matchId: "u1v2w3x4", drawPoints: 180, guessPoints: 70, outcome: "Win" },
  { matchId: "y5z6a7b8", drawPoints: 95, guessPoints: 40, outcome: "Lose" },
  { matchId: "c9d0e1f2", drawPoints: 130, guessPoints: 65, outcome: "Win" },
  { matchId: "g3h4i5j6", drawPoints: 160, guessPoints: 55, outcome: "Lose" },
  { matchId: "k7l8m9n0", drawPoints: 210, guessPoints: 90, outcome: "Win" },
  { matchId: "o1p2q3r4", drawPoints: 175, guessPoints: 60, outcome: "Lose" },
  { matchId: "s5t6u7v8", drawPoints: 110, guessPoints: 35, outcome: "Lose" },
  { matchId: "w9x0y1z2", drawPoints: 190, guessPoints: 75, outcome: "Win" },
  { matchId: "b3c4d5e6", drawPoints: 145, guessPoints: 50, outcome: "Lose" },
  { matchId: "f7g8h9i0", drawPoints: 170, guessPoints: 85, outcome: "Win" },
];

export const leaderboardData = [
  { rank: 1, username: "PlayerOne", score: 1300 },
  { rank: 2, username: "SketchMaster", score: 1250 },
  { rank: 3, username: "DrawKing", score: 1200 },
  { rank: 4, username: "Random1", score: 1100 },
  { rank: 5, username: "Random2", score: 1000 },
  { rank: 6, username: "Random3", score: 900 },
];

export const leaderboardColumns = [
  { key: "rank", label: "Rank" },
  { key: "username", label: "Username" },
  { key: "score", label: "Score" },
];

export const chartData = [
  { day: "Mon", games: 4 },
  { day: "Tue", games: 6 },
  { day: "Wed", games: 3 },
  { day: "Thu", games: 8 },
  { day: "Fri", games: 5 },
  { day: "Sat", games: 10 },
  { day: "Sun", games: 7 },
];
