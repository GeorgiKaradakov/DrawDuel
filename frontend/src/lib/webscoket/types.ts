import type { GuessEntry } from "../globalTypes";

export interface DrawEvent {
  tool: { name: string; strokeWidth: number };
  color: string;
  points: number[];
}

export interface ServerMessage {
  type: string;
  gameId?: string;
  role?: "drawer" | "guesser";
  words?: string[];
  word?: string;
  wordLength?: number;
  playerName?: string;
  guess?: string;
  correct?: boolean;
  countdown?: number;
  roundNumber?: number;
}

export interface GameDrawingCanvasProps {
  isDrawer: boolean;
  className?: string;
}

export interface GuessMessageContProps {
  className?: string;
  guessMessages: GuessEntry[];
  isDrawer?: boolean;
}
