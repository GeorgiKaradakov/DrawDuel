import { cn, tools } from "@/lib/utils";
import DrawLogic from "@/pages/Game/Drawing";
import { useState } from "react";
import { object } from "zod";

const ToolPicker = ({
  className,
  onToolSelect,
}: {
  className: string;
  onToolSelect: (tool: { name: string; strokeWidth: number }) => void;
}) => {
  const [lastSelected, setLastSelected] = useState<number>(0);
  return (
    <div
      className={cn(
        "flex flex-col items-center justify-evenly bg-neutral-700 rounded-xl",
        className,
      )}
    >
      {Object.entries(tools).map(([name, tool], idx) => (
        <div
          className={`p-2 w-12 h-12 rounded-xl hover:cursor-pointer hover:bg-neutral-500 duration-200 ease-in-out ${lastSelected === idx ? "bg-neutral-500" : "bg-transparent"}`}
          onClick={() => {
            setLastSelected(idx);
            onToolSelect({
              name: tool.name,
              strokeWidth: tool.strokeWidth,
            });
          }}
          key={name}
        >
          <tool.icon className="w-full h-full" />
        </div>
      ))}
    </div>
  );
};

export default ToolPicker;
