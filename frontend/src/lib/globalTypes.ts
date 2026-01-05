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
