import { cn } from "@/lib/utils";
import type { TimerProps } from "./types";
import { useEffect, useState } from "react";
import { AlarmClock } from "lucide-react";

function displayNumbers(num: number) {
  return num < 10 ? `0${num}` : num;
}

const GameTimer = ({ className, countDownSeconds, onTimeUp }: TimerProps) => {
  const [minutes, setMinutes] = useState(0);
  const [seconds, setSeconds] = useState(0);

  useEffect(() => {
    let remainingSeconds = countDownSeconds;

    const mins = Math.floor(remainingSeconds / 60);
    const secs = remainingSeconds % 60;

    setMinutes(mins);
    setSeconds(secs);

    const interval = setInterval(() => {
      const mins = Math.floor(remainingSeconds / 60);
      const secs = remainingSeconds % 60;

      setMinutes(mins);
      setSeconds(secs);

      if (remainingSeconds <= 0) {
        if (onTimeUp) onTimeUp();

        clearInterval(interval);
      } else {
        remainingSeconds--;
      }
    }, 1000);

    return () => clearInterval(interval);
  }, [countDownSeconds]);

  return (
    <div
      className={cn(
        "w-35 h-15 flex justify-center items-center gap-2 border border-neutral-400 rounded-xl",
        className,
      )}
    >
      <AlarmClock className="w-8 h-8 text-neutral-50" />
      <p className="text-semibold text-2xl text-neutral-50">
        {displayNumbers(minutes)}:{displayNumbers(seconds)}
      </p>
    </div>
  );
};

export default GameTimer;
