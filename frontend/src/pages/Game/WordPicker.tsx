import { chooseWord, chooseWordTimeOut } from "@/lib/webscoket/gameSocket";
import type { WordPickerProps } from "./types";
import GameTimer from "@/components/layout/Game/GameTimer";
import { useRef } from "react";

const WordPicker = ({ words }: WordPickerProps) => {
  const hasChosen = useRef(false);

  const handleWordClick = (word: string) => {
    chooseWord(word);
    document.getElementById("word-picker")!.classList.add("hidden");
  };

  const handleTimeUp = () => {
    if (hasChosen.current) return;
    hasChosen.current = true;
    chooseWordTimeOut(words);
  };

  return (
    <div
      id="word-picker"
      className="fixed inset-0 flex flex-col justify-evenly items-center bg-black/80 z-10"
    >
      <GameTimer countDownSeconds={10} onTimeUp={handleTimeUp} />
      <p className="text-4xl text-neutral-50 font-bold">Pick a word to draw!</p>
      <div className="w-2/3 h-3/5 grid grid-cols-2 gap-2 place-items-center">
        {words.map((w, i) => (
          <button
            key={i}
            onClick={() => handleWordClick(w)}
            className="w-2/3 h-2/3 text-3xl font-semibold bg-neutral-800 text-white rounded-lg hover:bg-neutral-700 transition"
          >
            {w}
          </button>
        ))}
      </div>
    </div>
  );
};

export default WordPicker;
