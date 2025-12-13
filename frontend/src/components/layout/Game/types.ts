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
