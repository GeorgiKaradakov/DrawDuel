import WordDisplay from "@/components/layout/Game/WordDisplay";
import WordPicker from "./WordPicker";
import GameTimer from "@/components/layout/Game/GameTimer";
import GuessMessageCont from "@/components/layout/Game/GuessMessageCont";
import PresentScores from "@/components/layout/Game/PresentScores";
import PresentWinner from "@/components/layout/Game/PresentWinner";
import { useNavigate } from "react-router";
import { TimerProvider } from "@/context/TimerContext";
import GameDrawingCanvas from "@/components/layout/Game/GameDrawingCanvas";
import { useGame } from "./context/useGame";
import { sendLeaveGame } from "@/lib/webscoket/gameSocket";

const GameDraw = () => {
  const { state, dispatch } = useGame();
  const navigate = useNavigate();

  if (state.endGame) {
    return (
      <PresentWinner
        playerAName={state.endGameStats.playerAName}
        playerBName={state.endGameStats.playerBName}
        playerAScore={state.endGameStats.playerAScore}
        playerBScore={state.endGameStats.playerBScore}
        winner={state.endGameStats.winner}
        onPlayAgain={function (): void {
          dispatch({ type: "RESET" });
          sendLeaveGame();
          navigate(`/find-game/${state.endGameStats.playerId}`);
        }}
        onReturnToDashboard={function (): void {
          dispatch({ type: "RESET" });
          sendLeaveGame();
          navigate("/dashboard");
        }}
      />
    );
  }

  return (
    <TimerProvider>
      {state.choosingWord && (
        <WordPicker
          words={state.words}
          wordPickTime={state.wordPickCountDown}
        />
      )}
      <>
        {state.roundStarted && (
          <WordDisplay
            className="top-20 w-1/4 border-0"
            word={state.chosenWord!}
          />
        )}
        <GameDrawingCanvas isDrawer={true} />
        {state.roundStarted && (
          <GameTimer className="" countDownSeconds={state.roundTime} />
        )}
        <GuessMessageCont
          className="z-10 w-1/4 h-1/3 bg-black/30 backdrop-blur-sm"
          guessMessages={state.guesses}
          isDrawer={true}
        />
      </>
      {state.roundEnded && <PresentScores roundResults={state.roundResults!} />}
    </TimerProvider>
  );
};

export default GameDraw;
