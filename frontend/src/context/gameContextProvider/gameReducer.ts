import { initialGameState, type Action, type GameState } from "./types";

export function reducer(state: GameState, action: Action): GameState {
  switch (action.type) {
    case "WORD_CHOICE":
      return {
        ...state,
        gameId: action.payload.gameId!,
        role: "drawer",
        words: action.payload.words ?? [],
        wordPickCountDown: action.payload.countdown ?? 10,
        choosingWord: true,
        roundStarted: false,
        roundEnded: false,
        guesses: [],
      };

    case "WAITING_FOR_WORD":
      return {
        ...state,
        gameId: action.payload.gameId!,
        role: "guesser",
        choosingWord: false,
      };

    case "START_ROUND":
      return {
        ...state,
        choosingWord: false,
        roundStarted: true,
        roundTime: action.payload.countdown ?? 60,
        chosenWord: action.payload.word,
        maskedWord: action.payload.wordLength
          ? "_ ".repeat(action.payload.wordLength)
          : undefined,
      };

    case "GUESS_MESSAGE":
      return {
        ...state,
        guesses: [...state.guesses, action.payload],
      };

    case "ROUND_END":
      return {
        ...state,
        roundStarted: false,
        roundEnded: true,
      };

    case "GAME_END":
      return {
        ...state,
        endGame: true,
        endGameStats: action.payload,
      };

    case "RESET":
      return initialGameState;

    default:
      return state;
  }
}
