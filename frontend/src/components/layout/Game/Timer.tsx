import { cn } from "@/lib/utils";
import type { TimerProps } from "./types";

const Timer = ({ className }: TimerProps) => {
  return <div className={cn("", className)}></div>;
};

export default Timer;
