import { sendDrawEvent } from "@/lib/webscoket/gameSocket";
import {
  addSocketListener,
  removeSocketListener,
} from "@/lib/webscoket/socketManager";
import { useEffect, useRef, useState } from "react";

export default function DrawLogic(isDrawer: boolean) {
  const [tool, setTool] = useState<{ name: string; strokeWidth: number }>({
    name: "pen",
    strokeWidth: 5,
  });
  const [lines, setLines] = useState<any[]>([]);
  const [color, setColor] = useState<string>("#ffffff");
  const isDrawing = useRef(false);

  useEffect(() => {
    const handleDraw = (data: any) => {
      if (data.tool && data.points) setLines((prev) => [...prev, data]);
    };
    addSocketListener(handleDraw);
    return () => removeSocketListener(handleDraw);
  }, []);

  const handleToolChange = (newTool: { name: string; strokeWidth: number }) => {
    setTool(newTool);
  };

  const handleColorChange = (newColor: string) => {
    setColor(newColor);
  };

  const handleMouseDown = (e: any) => {
    if (!isDrawer) return;

    isDrawing.current = true;
    const pos = e.target.getStage().getPointerPosition();
    setLines([...lines, { tool, color, points: [pos.x, pos.y] }]);
  };

  const handleMouseMove = (e: any) => {
    if (!isDrawer) return;

    if (!isDrawing.current) return;

    const stage = e.target.getStage();
    const point = stage.getPointerPosition();
    let lastLine = lines[lines.length - 1];

    lastLine.points = lastLine.points.concat([point.x, point.y]);

    lines.splice(lines.length - 1, 1, lastLine);
    setLines(lines.concat());
    sendDrawEvent({
      tool: lastLine.tool,
      color: lastLine.color,
      points: lastLine.points,
    });
  };

  const handleMouseUp = () => {
    if (!isDrawer) return;

    isDrawing.current = false;
  };

  return {
    tool,
    lines,
    color,
    handleMouseDown,
    handleMouseMove,
    handleMouseUp,
    handleColorChange,
    handleToolChange,
  };
}
