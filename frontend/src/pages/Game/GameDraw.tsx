import { useEffect, useState } from "react";
import GameDrawingCanvas from "@/components/layout/Game/GameDrawingCanvas";
import GuessMessageCont from "@/components/layout/Game/GuessMessageCont";
import {
  addSocketListener,
  disconnectSocket,
  removeSocketListener,
} from "@/lib/webscoket/socketManager";
import {
  type RoundResults,
  type GuessEntry,
  type PresentWinnerProps,
} from "@/lib/globalTypes";
import WordPicker from "@/pages/Game/WordPicker";
import GameTimer from "@/components/layout/Game/GameTimer";
import WordDisplay from "@/components/layout/Game/WordDisplay";
import { TimerProvider } from "@/context/TimerContext";
import PresentScores from "@/components/layout/Game/PresentScores";
import { useLocation, useNavigate } from "react-router";
import PresentWinner from "@/components/layout/Game/PresentWinner";

const GameDraw = () => {
  const { state } = useLocation();
  const initialWords = state?.words || [];
  const initialWordPickCountDown = state?.wordPickCountDown || 10;

  const navigate = useNavigate();

  const [words, setWords] = useState<string[]>(initialWords);
  const [wordPickCountDown, setWordPickCountDown] = useState(
    initialWordPickCountDown,
  );
  const [choosingWord, setChoosingWord] = useState(initialWords.length > 0);
  const [guesses, setGuesses] = useState<GuessEntry[]>([]);
  const [chosenWord, setChosenWord] = useState<string>(""); // 🆕
  const [roundStarted, setRoundStarted] = useState(false);
  const [roundEnded, setRoundEnded] = useState(false);
  const [roundTime, setRoundTime] = useState(0);
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
    setChosenWord?.("");

    const handleSocketMessage = (data: any) => {
      if (data.type === "wordChoice" && data.role === "drawer") {
        setWords(data.words);
        setWordPickCountDown(data.countdown || 10);
        setChoosingWord(true);

        setChosenWord("");
        setRoundStarted(false);
        setRoundEnded(false);
      } else if (data.type === "waitingForWord" && data.role === "guesser") {
        navigate(`/game/${data.gameId}/guess`);
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

      if (data.type === "startRound" && data.role === "drawer") {
        setChoosingWord(false);
        setChosenWord(data.word);
        setRoundStarted(true);
        setRoundTime(data.countdown || 60);
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

    addSocketListener(handleSocketMessage);
    return () => removeSocketListener(handleSocketMessage);
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
          console.log(endGameStats.playerId);
          navigate(`/find-game/${endGameStats.playerId}`);
          disconnectSocket();
        }}
        onReturnToDashboard={function (): void {
          navigate("/dashboard");
        }}
      />
    );
  }

  return (
    <TimerProvider>
      {choosingWord && (
        <WordPicker words={words} wordPickTime={wordPickCountDown} />
      )}
      <>
        {roundStarted && <WordDisplay className="top-20" word={chosenWord} />}
        <GameDrawingCanvas isDrawer={true} />
        {roundStarted && (
          <GameTimer className="" countDownSeconds={roundTime} />
        )}
        <GuessMessageCont
          className="z-10 w-1/4 h-1/3 bg-black/30 backdrop-blur-sm"
          guessMessages={guesses}
          isDrawer={true}
        />
      </>
      {roundEnded && <PresentScores roundResults={roundResults!} />}
    </TimerProvider>
  );
};

export default GameDraw;
