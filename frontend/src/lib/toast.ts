import { toast } from "sonner";
import type React from "react";

export const successToast = (message: string) => {
  toast.success(message, {
    style: {
      "--normal-bg":
        "color-mix(in oklab, light-dark(var(--color-green-600), var(--color-green-400)) 10%, var(--background))",
      "--normal-text":
        "light-dark(var(--color-green-600), var(--color-green-400))",
      "--normal-border":
        "light-dark(var(--color-green-600), var(--color-green-400))",
    } as React.CSSProperties,
  });
};

export const errorToast = (message: string) => {
  toast.error(message, {
    style: {
      "--normal-bg":
        "color-mix(in oklab, var(--destructive) 10%, var(--background))",
      "--normal-text": "var(--destructive)",
      "--normal-border": "var(--destructive)",
    } as React.CSSProperties,
  });
};
