import GameDrawingCanvas from "@/components/layout/Game/GameDrawingCanvas";
import GuessMessageCont from "@/components/layout/Game/GuessMessageCont";
import { useRef } from "react";

const GameGuess = () => {
  const guesses: string[] = [
    "someUser121 has incorrectly guessed 'apple'",
    "someUser121 has incorrectly guessed 'orange'",
  ];
  return (
    <div className="w-screen h-screen">
      <GameDrawingCanvas className="z-20" isDrawer={false} />
      <GuessMessageCont
        className="z-10 w-1/4 h-1/3 bg-black/30 backdrop-blur-sm"
        guessMessages={guesses}
      />
    </div>
  );
};

export default GameGuess;
