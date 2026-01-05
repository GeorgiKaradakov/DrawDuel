import { useEffect, useState } from "react";
import GameDrawingCanvas from "@/components/layout/Game/GameDrawingCanvas";
import GuessMessageCont from "@/components/layout/Game/GuessMessageCont";
import {
  addSocketListener,
  removeSocketListener,
} from "@/lib/webscoket/socketManager";
import { type RoundResults, type GuessEntry } from "@/lib/globalTypes";
import WordPicker from "@/pages/Game/WordPicker";
import GameTimer from "@/components/layout/Game/GameTimer";
import WordDisplay from "@/components/layout/Game/WordDisplay";
import { TimerProvider } from "@/context/TimerContext";
import PresentScores from "@/components/layout/Game/PresentScores";
import { useLocation, useNavigate } from "react-router";

const GameDraw = () => {
  const { state } = useLocation();
  const initialWords = state?.words || [];

  const navigate = useNavigate();

  const [words, setWords] = useState<string[]>(initialWords);
  const [choosingWord, setChoosingWord] = useState(initialWords.length > 0);
  const [guesses, setGuesses] = useState<GuessEntry[]>([]);
  const [chosenWord, setChosenWord] = useState<string>(""); // 🆕
  const [roundStarted, setRoundStarted] = useState(false);
  const [roundEnded, setRoundEnded] = useState(false);
  const [roundTime, setRoundTime] = useState(60);
  const [roundResults, setRoundResults] = useState<RoundResults | null>(null);

  useEffect(() => {
    const handleSocketMessage = (data: any) => {
      if (data.type === "wordChoice" && data.role === "drawer") {
        setWords(data.words);
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

      if (data.type === "startRound") {
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
    };

    addSocketListener(handleSocketMessage);
    return () => removeSocketListener(handleSocketMessage);
  }, []);

  return (
    <TimerProvider>
      {choosingWord && <WordPicker words={words} />}
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
