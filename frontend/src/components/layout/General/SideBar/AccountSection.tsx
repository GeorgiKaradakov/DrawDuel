import { cn } from "@/lib/utils";
import type { AccountSectionProps } from "./types";
import { LogOut } from "lucide-react";
import { useAuth } from "@/context/authProvider/useAuth";

const AccountSection = ({ className }: AccountSectionProps) => {
  const { logout, user } = useAuth();

  return (
    <div
      className={cn(
        "px-3 w-full h-20 flex justify-between items-center space-x-3",
        className,
      )}
    >
      <div className="flex justify-center items-center space-x-2">
        <img
          className="rounded-full"
          src={user?.profileImageUrl || undefined}
          alt="Profile Image"
          width={50}
          height={50}
        />
        <div className="flex flex-col justify-center items-start w-full h-full">
          <p className="text-md text-neutral-50 font-bold">{user?.username}</p>
          <p className="text-sm text-neutral-200">{user?.email}</p>
        </div>
      </div>

      <div
        className="p-1 hover:bg-neutral-300/20 rounded-lg hover:scale-102 transition-all ease-in-out transition-duration-300 hover:cursor-pointer"
        onClick={logout}
      >
        <LogOut className="w-6 h-6 text-neutral-50" />
      </div>
    </div>
  );
};

export default AccountSection;
