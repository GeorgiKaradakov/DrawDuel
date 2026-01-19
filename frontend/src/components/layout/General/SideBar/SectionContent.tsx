import { cn } from "@/lib/utils";
import type { SectionContentProps } from "./types";

const SectionContent = ({
  className,
  name,
  icon,
  handleOnClick,
}: SectionContentProps) => {
  return (
    <div
      className={cn(
        "px-3 py-1 ml-10 w-fit h-fit flex justify-start items-start rounded-lg hover:bg-neutral-600 hover:cursor-pointer",
        className,
      )}
      onClick={handleOnClick}
    >
      {icon && icon}
      <p className="text-lg text-neutral-50 font-semibold">{name}</p>
    </div>
  );
};

export default SectionContent;
