import { cn } from "@/lib/utils";
import type { SectionContentProps } from "./types";
import { useNavigate } from "react-router";

const SectionContent = ({ className, name, url }: SectionContentProps) => {
  const navigate = useNavigate();

  const handleClick = (url: string) => {
    navigate(url);
  };

  return (
    <div
      className={cn(
        "p-3 ml-10 w-fit h-fit flex justify-start items-start rounded-lg hover:bg-neutral-600 hover:cursor-pointer",
        className,
      )}
      onClick={() => handleClick(url)}
    >
      <p className="text-lg text-neutral-50 font-semibold">{name}</p>
    </div>
  );
};

export default SectionContent;
