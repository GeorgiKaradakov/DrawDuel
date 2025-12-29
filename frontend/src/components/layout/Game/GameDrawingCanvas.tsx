import DrawLogic from "@/pages/Game/Drawing";
import { Layer, Line, Stage } from "react-konva";
import ToolPicker from "./ToolPicker";
import ColorPicker from "./ColorPicker";
import type { GameDrawingCanvasProps } from "@/lib/webscoket/types";
import { cn } from "@/lib/utils";

const GameDrawingCanvas = ({ isDrawer, className }: GameDrawingCanvasProps) => {
  const {
    handleMouseDown,
    handleMouseMove,
    handleMouseUp,
    handleToolChange,
    handleColorChange,
    lines,
  } = DrawLogic(isDrawer);

  return (
    <>
      <Stage
        width={window.innerWidth}
        height={window.innerHeight}
        onMouseDown={handleMouseDown}
        onMouseMove={handleMouseMove}
        onMouseUp={handleMouseUp}
        className={cn(className, "bg-neutral-600")}
      >
        <Layer>
          {lines.map((line, idx) => {
            console.log(line);
            return (
              <Line
                key={idx}
                points={line.points}
                stroke={line.color}
                strokeWidth={line.tool.strokeWidth}
                tension={0.5}
                lineCap="round"
                lineJoin="round"
                globalCompositeOperation={
                  line.tool.name === "eraser"
                    ? "destination-out"
                    : "source-over"
                }
              />
            );
          })}
        </Layer>
      </Stage>

      {isDrawer && (
        <div className="absolute w-15 h-3/8 left-2 top-1/2 flex flex-col items-center justify-between -translate-y-1/2 rounded-lg space-y-2">
          <ColorPicker
            className="w-full h-2/3"
            OnColorSelect={handleColorChange}
          />
          <ToolPicker
            className="w-full h-1/3"
            onToolSelect={handleToolChange}
          />
        </div>
      )}
    </>
  );
};

export default GameDrawingCanvas;
