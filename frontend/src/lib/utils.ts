import { clsx, type ClassValue } from "clsx";
import { Eraser, PenLine } from "lucide-react";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export const colors = {
  black: "#000000",
  white: "#ffffff",
  red: "#ef4444",
  green: "#22c55e",
  blue: "#3b82f6",
  yellow: "#facc15",
};

export const tools = {
  pen: { name: "pen", icon: PenLine, strokeWidth: 5 },
  eraser: { name: "eraser", icon: Eraser, strokeWidth: 40 },
};
