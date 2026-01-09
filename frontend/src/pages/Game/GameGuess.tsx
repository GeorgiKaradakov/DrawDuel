import { useEffect, useState } from "react";
import GameDrawingCanvas from "@/components/layout/Game/GameDrawingCanvas";
import GuessMessageCont from "@/components/layout/Game/GuessMessageCont";
import GameTimer from "@/components/layout/Game/GameTimer";
import {
  addSocketListener,
  disconnectSocket,
  removeSocketListener,
} from "@/lib/webscoket/socketManager";
import type {
  GuessEntry,
  PresentWinnerProps,
  RoundResults,
} from "@/lib/globalTypes";
import WordDisplay from "@/components/layout/Game/WordDisplay";
import GuesserWaitForWordAnimation from "@/components/layout/Game/GuesserWaitForWordAnimation";
import { TimerProvider } from "@/context/TimerContext";
import PresentScores from "@/components/layout/Game/PresentScores";
import { useNavigate } from "react-router";
import { sentRoundTimeOut } from "@/lib/webscoket/gameSocket";
import PresentWinner from "@/components/layout/Game/PresentWinner";

const GameGuess = () => {
  const navigate = useNavigate();
  const [guesses, setGuesses] = useState<GuessEntry[]>([]);
  const [waiting, setWaiting] = useState(true);
  const [roundStarted, setRoundStarted] = useState(false);
  const [roundTime, setRoundTime] = useState(60);
  const [maskedWord, setMaskedWord] = useState<string>("");
  const [roundEnded, setRoundEnded] = useState(false);
  const [roundResults, setRoundResults] = useState<RoundResults | null>(null);
  const [endGameStats, setEndGameStats] = useState<PresentWinnerProps>({
    playerAName: "",
    playerBName: "",
    playerAScore: 0,
    playerBScore: 0,
    playerId: "",
    winner: "",
  });
  const [endGame, setEndGame] = useState(false);

  useEffect(() => {
    setRoundStarted(false);
    setRoundEnded(false);
    const handleSocket = (data: any) => {
      if (data.type === "waitingForWord" && data.role === "guesser") {
        setWaiting(true);
      } else if (data.type === "wordChoice" && data.role === "drawer") {
        console.log(data);
        navigate(`/game/${data.gameId}/draw`, {
          state: { words: data.words, wordPickCountDown: data.countdown },
        });
      }

      if (data.type === "startRound" && data.role === "guesser") {
        setWaiting(false);
        setRoundStarted(true);
        setRoundTime(data.countdown || 60);
        if (data.wordLength) {
          setMaskedWord("_ ".repeat(data.wordLength));
        }
      }

      if (data.type === "guessMessage") {
        setGuesses((prev) => [
          ...prev,
          {
            playerName: data.playerName,
            guess: data.guess,
            correct: data.correct,
          },
        ]);
      }

      if (data.type === "roundEnd") {
        setRoundStarted(false);
        setTimeout(() => {
          setRoundEnded(true);
          setRoundResults({
            drawerName: data.drawerName,
            guesserName: data.guesserName,
            drawerScore: data.drawerScore,
            guesserScore: data.guesserScore,
          });
        }, 500);
      }

      if (data.type === "gameEnd") {
        setEndGame(true);
        setEndGameStats({
          playerAName: data.playerAName,
          playerBName: data.playerBName,
          playerAScore: data.playerAScore,
          playerBScore: data.playerBScore,
          playerId: data.playerId,
          winner: data.winner,
        });
      }
    };

    addSocketListener(handleSocket);
    return () => removeSocketListener(handleSocket);
  }, []);

  if (endGame) {
    return (
      <PresentWinner
        playerAName={endGameStats.playerAName}
        playerBName={endGameStats.playerBName}
        playerAScore={endGameStats.playerAScore}
        playerBScore={endGameStats.playerBScore}
        winner={endGameStats.winner}
        onPlayAgain={function (): void {
          navigate(`/find-game/${endGameStats.playerId}`);
          disconnectSocket();
        }}
        onReturnToDashboard={function (): void {
          navigate("/dashboard");
        }}
      />
    );
  }

  if (waiting && !roundStarted) {
    return <GuesserWaitForWordAnimation />;
  }

  return (
    <TimerProvider>
      <div className="w-screen h-screen">
        <>
          <WordDisplay
            className="top-20"
            word={maskedWord || "???"}
            isMasked={true}
          />
          <GameDrawingCanvas isDrawer={false} />
          <GameTimer
            countDownSeconds={roundTime}
            onTimeUp={() => sentRoundTimeOut(guesses.length)}
          />
          <GuessMessageCont
            className="z-10 w-1/4 h-1/3 bg-black/30 backdrop-blur-sm"
            guessMessages={guesses}
            isDrawer={false}
          />
        </>
        {roundEnded && <PresentScores roundResults={roundResults!} />}
      </div>
    </TimerProvider>
  );
};

export default GameGuess;
