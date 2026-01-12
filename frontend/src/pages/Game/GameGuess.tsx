import GuesserWaitForWordAnimation from "@/components/layout/Game/GuesserWaitForWordAnimation";
import { useGame } from "./context/useGame";
import WordDisplay from "@/components/layout/Game/WordDisplay";
import GameDrawingCanvas from "@/components/layout/Game/GameDrawingCanvas";
import GameTimer from "@/components/layout/Game/GameTimer";
import { sendLeaveGame, sentRoundTimeOut } from "@/lib/webscoket/gameSocket";
import GuessMessageCont from "@/components/layout/Game/GuessMessageCont";
import { useNavigate } from "react-router";
import PresentWinner from "@/components/layout/Game/PresentWinner";
import { TimerProvider } from "@/context/TimerContext";
import PresentScores from "@/components/layout/Game/PresentScores";

const GameGuess = () => {
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

  if (state.waitForWord) {
    return <GuesserWaitForWordAnimation />;
  }

  return (
    <TimerProvider>
      <WordDisplay
        className="top-20 w-1/4 border-0"
        word={state.maskedWord || "???"}
        isMasked
      />
      <GameDrawingCanvas isDrawer={false} />
      <GameTimer
        countDownSeconds={state.roundTime}
        onTimeUp={() => sentRoundTimeOut(state.guesses.length)}
      />
      <GuessMessageCont
        className="z-10 w-1/4 h-1/3 bg-black/30 backdrop-blur-sm"
        guessMessages={state.guesses}
      />
      {state.roundEnded && <PresentScores roundResults={state.roundResults!} />}
    </TimerProvider>
  );
};

export default GameGuess;
