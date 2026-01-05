import type { RoundResults } from "@/lib/globalTypes";

export interface ToolPickerProps {
  className?: string;
  onToolSelect: (tool: { name: string; strokeWidth: number }) => void;
}

export interface ColorPickerProps {
  className?: string;
  OnColorSelect: (color: string) => void;
}

export interface TimerProps {
  className?: string;
  countDownSeconds: number;
  onTimeUp?: () => void;
}

export interface WordContainerProps {
  className?: string;
  word: string;
}

export interface WordDisplayProps {
  className?: string;
  word: string;
  isMasked?: boolean;
}

export interface PresentScoresProps {
  className?: string;
  roundResults: RoundResults;
}
