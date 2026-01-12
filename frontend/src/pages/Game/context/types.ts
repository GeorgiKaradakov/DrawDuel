import type { ServerMessage } from "@/lib/webscoket/types";

export interface GameState {
  role?: "drawer" | "guesser";
  gameId?: string;

  // word phase
  words: string[];
  wordPickCountDown: number;
  choosingWord: boolean;
  waitForWord: boolean;

  // round
  roundStarted: boolean;
  roundTime: number;
  chosenWord?: string;
  maskedWord?: string;

  // guesses & results
  guesses: any[];
  roundEnded: boolean;
  roundResults?: {
    drawerName: string;
    guesserName: string;
    drawerScore: number;
    guesserScore: number;
  };

  // game end
  endGame: boolean;
  endGameStats?: any;
}

export const initialGameState: GameState = {
  words: [],
  wordPickCountDown: 10,
  choosingWord: false,
  waitForWord: false,

  roundStarted: false,
  roundTime: 0,

  guesses: [],
  roundEnded: false,

  endGame: false,
};

export type Action =
  | { type: "WORD_CHOICE"; payload: ServerMessage }
  | { type: "WAITING_FOR_WORD"; payload: ServerMessage }
  | { type: "START_ROUND"; payload: ServerMessage }
  | { type: "GUESS_MESSAGE"; payload: any }
  | { type: "ROUND_END"; payload: any }
  | { type: "GAME_END"; payload: any }
  | { type: "RESET" };

export interface GameProviderProps {
  gameId: string;
  token: string;
  children: React.ReactNode;
}
