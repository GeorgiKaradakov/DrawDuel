import Timer from "@/components/layout/Game/GameTimer";
import WordPicker from "@/pages/Game/WordPicker";
import GameDrawingCanvas from "@/components/layout/Game/GameDrawingCanvas";

const GameDraw = () => {
  return (
    <>
      <WordPicker />

      <GameDrawingCanvas isDrawer={true} />

      <Timer className="" countDownSeconds={0} />
    </>
  );
};

export default GameDraw;
