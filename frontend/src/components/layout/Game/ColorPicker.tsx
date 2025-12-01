import { cn, colors } from "@/lib/utils";
import DrawLogic from "@/pages/Game/Drawing";

const ColorPicker = ({
  className,
  onColorSelect,
}: {
  className: string;
  onColorSelect: (color: string) => void;
}) => {
  return (
    <div
      className={cn(
        "flex flex-col bg-neutral-700 items-center justify-evenly rounded-xl",
        className,
      )}
    >
      {Object.entries(colors).map(([hex]) => (
        <div
          className="w-10 h-10 rounded-xl hover:cursor-pointer hover:scale-105 duration-200 ease-in-out"
          onClick={() => {
            onColorSelect(hex);
          }}
          style={{ backgroundColor: hex }}
          key={hex}
        ></div>
      ))}
    </div>
  );
};

export default ColorPicker;
