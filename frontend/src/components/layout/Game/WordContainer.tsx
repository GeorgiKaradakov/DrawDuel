import { cn } from "@/lib/utils";
import type { WordContainerProps } from "./types";

const WordContainer = ({ word, className }: WordContainerProps) => {
  return (
    <div
      className={cn(
        "flex justify-center items-center bg-neutral-500 rounded-lg hover:scale-105 hover:cursor-pointer",
        className,
      )}
    >
      <p className="text-4xl text-neutral-50 font-semibold">{word}</p>
    </div>
  );
};

export default WordContainer;
