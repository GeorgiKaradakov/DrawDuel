import { cn } from "@/lib/utils";
import type { TimerProps } from "./types";
import { useEffect, useState } from "react";
import { AlarmClock } from "lucide-react";
import { useTimer } from "@/context/TimerContext";

function displayNumbers(num: number) {
  return num < 10 ? `0${num}` : num;
}

const GameTimer = ({ className, countDownSeconds, onTimeUp }: TimerProps) => {
  const [minutes, setMinutes] = useState(0);
  const [seconds, setSeconds] = useState(0);

  const { setTimeLeft } = useTimer();

  useEffect(() => {
    let remainingSeconds = countDownSeconds;
    setTimeLeft(remainingSeconds);

    const mins = Math.floor(remainingSeconds / 60);
    const secs = remainingSeconds % 60;

    setMinutes(mins);
    setSeconds(secs);

    const interval = setInterval(() => {
      const mins = Math.floor(remainingSeconds / 60);
      const secs = remainingSeconds % 60;

      setMinutes(mins);
      setSeconds(secs);
      setTimeLeft(remainingSeconds);

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
        "w-35 h-15 fixed top-4 left-1/2 transform -translate-x-1/2 flex justify-center items-center gap-2 border border-neutral-400 rounded-xl z-50",
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
