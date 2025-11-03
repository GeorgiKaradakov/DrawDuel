import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";
import type { FormDescriptionToolTipProps } from "./types";
import { cn } from "@/lib/utils";

const FormDescrriptionToolTip = ({
  children,
  description,
  className,
  textStyle,
}: FormDescriptionToolTipProps) => {
  if (!description || description.trim().length <= 0) return children;

  return (
    <TooltipProvider>
      <Tooltip>
        <TooltipTrigger className="w-full h-full">{children}</TooltipTrigger>
        <TooltipContent
          side="top"
          align="center"
          className="w-xs text-center bg-neutral-800 rounded-md border border-neutral-400 p-2"
        >
          <p
            className={cn(
              "text-[1.03rem] py-2 font-semibold text-neutral-200 break-words",
              textStyle,
            )}
          >
            {description}
          </p>
        </TooltipContent>
      </Tooltip>
    </TooltipProvider>
  );
};

export default FormDescrriptionToolTip;
