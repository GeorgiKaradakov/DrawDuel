import {
  addSocketListener,
  connectSocket,
  removeSocketListener,
} from "@/lib/webscoket/socketManager";
import { initialGameState, type Action, type GameState } from "./types";
import { createContext, useEffect, useReducer } from "react";
import type { ServerMessage } from "@/lib/webscoket/types";
import { Outlet, useNavigate } from "react-router";

function reducer(state: GameState, action: Action): GameState {
  switch (action.type) {
    case "WORD_CHOICE":
      return {
        ...state,
        gameId: action.payload.gameId!,
        role: "drawer",
        words: action.payload.words ?? [],
        wordPickCountDown: action.payload.countdown ?? 10,
        choosingWord: true,
        waitForWord: false,
        roundStarted: false,
        roundEnded: false,
        guesses: [],
      };

    case "WAITING_FOR_WORD":
      return {
        ...state,
        gameId: action.payload.gameId!,
        role: "drawer",
        words: action.payload.words ?? [],
        wordPickCountDown: action.payload.countdown ?? 10,
        choosingWord: false,
        waitForWord: true,
        roundStarted: false,
        roundEnded: false,
        guesses: [],
      };

    case "START_ROUND":
      return {
        ...state,
        choosingWord: false,
        waitForWord: false,
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
        choosingWord: false,
        waitForWord: false,

        roundTime: 0,

        roundResults: {
          drawerName: action.payload.drawerName,
          guesserName: action.payload.guesserName,
          drawerScore: action.payload.drawerScore,
          guesserScore: action.payload.guesserScore,
        },
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

export const GameContext = createContext<
  | {
      state: GameState;
      dispatch: React.Dispatch<Action>;
    }
  | undefined
>(undefined);

const GameProvider = () => {
  const [state, dispatch] = useReducer(reducer, initialGameState);
  const navigate = useNavigate();

  useEffect(() => {
    console.log("Provider Mounted!!!");
    connectSocket();

    const handler = (data: ServerMessage) => {
      switch (data.type) {
        case "wordChoice":
          dispatch({ type: "WORD_CHOICE", payload: data });
          console.log(state);
          navigate(`/game/${data.gameId}/draw`);
          break;

        case "waitingForWord":
          dispatch({ type: "WAITING_FOR_WORD", payload: data });
          navigate(`/game/${data.gameId}/guess`);
          break;

        case "startRound":
          dispatch({ type: "START_ROUND", payload: data });
          break;

        case "guessMessage":
          dispatch({
            type: "GUESS_MESSAGE",
            payload: {
              playerName: data.playerName,
              guess: data.guess,
              correct: data.correct,
            },
          });
          break;

        case "roundEnd":
          dispatch({ type: "ROUND_END", payload: data });
          break;

        case "gameEnd":
          dispatch({ type: "GAME_END", payload: data });
          break;
      }
    };

    addSocketListener(handler);
    return () => removeSocketListener(handler);
  }, []);

  return (
    <GameContext.Provider value={{ state, dispatch }}>
      <Outlet />
    </GameContext.Provider>
  );
};

export default GameProvider;
