import { cn } from "@/lib/utils";
import type { WordDisplayProps } from "./types";

const WordDisplay = ({
  word,
  className,
  isMasked = false,
}: WordDisplayProps) => {
  return (
    <div
      className={cn(
        "w-35 h-15 fixed top-4 left-1/2 transform -translate-x-1/2 flex justify-center items-center gap-2 border border-neutral-400 rounded-xl z-50",
        className,
      )}
    >
      {isMasked ? (
        <p className="text-3xl font-bold text-neutral-50">{word}</p>
      ) : (
        <p className="text-3xl font-bold text-blue-500">{word}</p>
      )}
    </div>
  );
};

export default WordDisplay;
