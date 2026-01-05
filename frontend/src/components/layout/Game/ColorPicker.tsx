import { cn, colors } from "@/lib/utils";
import type { ColorPickerProps } from "./types";
import { useState } from "react";

const ColorPicker = ({ className, OnColorSelect }: ColorPickerProps) => {
  const [currColor, setCurrColor] = useState<string>("#ffffff");
  return (
    <div
      className={cn(
        "py-2 flex flex-col bg-neutral-700 items-center justify-evenly rounded-xl",
        className,
      )}
    >
      {Object.entries(colors).map(([name, hex]) => (
        <div
          className={`w-10 h-10 rounded-xl hover:cursor-pointer hover:scale-105 duration-200 ease-in-out ${currColor === hex ? "border-3 border-amber-500" : "border-none"}`}
          onClick={() => {
            OnColorSelect(hex);
            setCurrColor(hex);
          }}
          style={{ backgroundColor: hex }}
          key={hex}
        ></div>
      ))}
    </div>
  );
};

export default ColorPicker;
