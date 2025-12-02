export interface ToolPickerProps {
  className?: string;
  onToolSelect: (tool: { name: string; strokeWidth: number }) => void;
}

export interface ColorPickerProps {
  className?: string;
  OnColorSelect: (color: string) => void;
}
