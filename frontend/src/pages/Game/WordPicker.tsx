import GameTimer from "@/components/layout/Game/GameTimer";
import WordContainer from "@/components/layout/Game/WordContainer";
import { useEffect, useRef } from "react";

const word = ["apple", "banana", "dog", "cat"];

const WordPicker = () => {
  const wordPickerRef = useRef<HTMLDivElement>(null);

  return (
    <div
      ref={wordPickerRef}
      className="fixed inset-0 flex flex-col justify-evenly items-center bg-black/60 z-10"
    >
      <GameTimer
        countDownSeconds={30}
        onTimeUp={() => {
          wordPickerRef.current!.hidden = true;
        }}
      />
      <p className="text-4xl text-neutral-50 font-bold">Pick a word to draw!</p>
      <div className="w-2/3 h-3/5 grid grid-cols-2 gap-2 place-items-center">
        {word.map((w, i) => (
          <WordContainer key={i} word={w} className={"w-2/3 h-2/3"} />
        ))}
      </div>
    </div>
  );
};

export default WordPicker;
