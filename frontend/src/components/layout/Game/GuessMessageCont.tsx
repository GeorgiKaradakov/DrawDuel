import { useTimer } from "@/context/TimerContext";
import { cn } from "@/lib/utils";
import { sendGuess } from "@/lib/webscoket/gameSocket";
import type { GuessMessageContProps } from "@/lib/webscoket/types";
import { Check, X } from "lucide-react";
import { useState } from "react";

const GuessMessageCont = ({
  guessMessages,
  className,
  isDrawer = false,
}: GuessMessageContProps) => {
  const [guess, setGuess] = useState("");
  const [guessCount, setGuessCount] = useState(0);

  const { timeLeft } = useTimer();

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter" && guess.trim() !== "") {
      e.preventDefault();
      setGuessCount((p) => p + 1);
      sendGuess(guess.trim(), timeLeft, guessCount);
      setGuess("");
    }
  };

  return (
    <div
      className={cn(
        "p-2 space-y-2 absolute top-[99%] left-[99.5%] flex flex-col justify-between items-center bg-transparent border-3 border-neutral-800 -translate-x-full -translate-y-full rounded-lg",
        className,
      )}
    >
      <div className="p-2 space-y-1 w-full h-4/5 border border-neutral-500 rounded-sm overflow-y-auto">
        {guessMessages.map((msg, index) => {
          const textColor = msg.correct ? "text-green-400" : "text-red-400";

          return (
            <p
              key={index}
              className={cn(
                "text-lg font-medium flex items-center gap-2",
                textColor,
              )}
            >
              <span className="text-purple-400 font-bold">
                {msg.playerName}
              </span>{" "}
              guessed <span className="text-blue-400 italic">{msg.guess}</span>
              {msg.correct ? (
                <Check className="w-5 h-5 bg-green-400 text-neutral-50 rounded-xs" />
              ) : (
                <X className="w-5 h-5 bg-red-400 text-neutral-50 rounded-xs" />
              )}
            </p>
          );
        })}
      </div>

      <div className="w-full h-1/5 border border-neutral-500 rounded-sm">
        <input
          type="text"
          placeholder={
            isDrawer ? "Drawers cannot type!" : "Enter your guess..."
          }
          value={guess}
          disabled={isDrawer}
          onChange={(e) => setGuess(e.target.value)}
          onKeyDown={handleKeyDown}
          className="pl-3 w-full h-full text-neutral-50 font-semibold text-xl outline-none bg-transparent"
        />
      </div>
    </div>
  );
};

export default GuessMessageCont;
