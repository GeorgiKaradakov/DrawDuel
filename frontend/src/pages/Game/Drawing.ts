import { sendDrawEvent } from "@/lib/webscoket/gameSocket";
import { getSocket } from "@/lib/webscoket/socketManager";
import { useEffect, useRef, useState } from "react";

export default function DrawLogic() {
  const [tool, setTool] = useState<{ name: string; strokeWidth: number }>({
    name: "pen",
    strokeWidth: 5,
  });
  const [lines, setLines] = useState<any[]>([]);
  const [color, setColor] = useState<string>("#ffffff");
  const isDrawing = useRef(false);

  useEffect(() => {
    const socket = getSocket();
    if (!socket) return;
    console.log(lines);
    socket.onmessage = (event) => {
      const data = JSON.parse(event.data);
      if (data.tool && data.points) setLines((prev) => [...prev, data]);
    };
  }, []);

  const handleToolChange = (newTool: { name: string; strokeWidth: number }) => {
    setTool(newTool);
  };

  const handleColorChange = (newColor: string) => {
    setColor(newColor);
  };

  const handleMouseDown = (e: any) => {
    isDrawing.current = true;
    const pos = e.target.getStage().getPointerPosition();
    setLines([...lines, { tool, color, points: [pos.x, pos.y] }]);
  };

  const handleMouseMove = (e: any) => {
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
