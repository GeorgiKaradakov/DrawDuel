import { cn } from "@/lib/utils";
import type { GuessMessageContProps } from "@/lib/webscoket/types";

const GuessMessageCont = ({
  guessMessages,
  className,
}: GuessMessageContProps) => {
  return (
    <div
      className={cn(
        "p-2 space-y-2 absolute top-[99%] left-[99.5%] flex flex-col justify-between items-center bg-transparent border-3 border-neutral-800 -translate-x-full -translate-y-full rounded-lg",
        className,
      )}
    >
      <div className="p-2 space-y-1 w-full h-4/5 border border-neutral-500 rounded-sm">
        {guessMessages.map((msg, index) => {
          const words = msg.split(" ");
          const [first, last] = [words[0], words[words.length - 1]];
          const rest = words.slice(1, words.length - 1).join(" ");
          return (
            <p key={index} className="text-lg text-neutral-50">
              <span className="text-red-400 font-bold">{first}</span> {rest}{" "}
              <span className="text-blue-400 font-semi-bold">{last}</span>
            </p>
          );
        })}
      </div>
      <div className="w-full h-1/5 border border-neutral-500 rounded-sm">
        <input
          className="pl-3 w-full h-full text-neutral-50 font-semibold text-xl outline-none"
          type="text"
          placeholder="Enter your guess here ..."
          name="guessInput"
        />
      </div>
    </div>
  );
};

export default GuessMessageCont;
