import { cn } from "@/lib/utils";
import type { SectionProps } from "./types";

const Section = ({ className, sectionName, children }: SectionProps) => {
  return (
    <div
      className={cn(
        "w-full flex flex-col justify-between items-center",
        className,
      )}
    >
      <div className="px-3 w-full h-fit">
        <p className="text-md text-neutral-500">{sectionName}</p>
      </div>

      <div className="w-full h-fit">{children}</div>
    </div>
  );
};

export default Section;
