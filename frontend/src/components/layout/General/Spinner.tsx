// src/components/ui/Spinner.tsx
import { cn } from "@/lib/utils";

export const Spinner = ({ className }: { className?: string }) => {
  return (
    <div
      className={cn(
        "h-5 w-5 animate-spin rounded-full border-2 border-white border-t-transparent",
        className,
      )}
    />
  );
};
