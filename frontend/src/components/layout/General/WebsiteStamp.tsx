import { cn } from "@/lib/utils";
import type { WebsiteStampProps } from "./types";

const WebsiteStamp = ({
  className,
  logoWidth,
  logoHeight,
}: WebsiteStampProps) => {
  return (
    <div className={cn("px-2 py-3 flex space-x-1", className)}>
      <img src="/logo.png" width={logoWidth} height={logoHeight} />
      <p className="text-neutral-200 font-bold">Draw Duel</p>
    </div>
  );
};

export default WebsiteStamp;
