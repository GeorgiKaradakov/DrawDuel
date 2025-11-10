import { Button } from "@/components/ui/button";
import { Outlet } from "react-router";

const AuthLayout = () => {
  return (
    <div className="relative w-screen h-screen overflow-hidden">
      <img
        src="/auth_background.png"
        className="absolute inset-0 w-full h-full object-fit z-0"
        alt="auth bg"
      />
      <div className="absolute inset-0 bg-gradient-to-b from-black/45 to-blue-500/45 backdrop-blur-xs z-5"></div>
      <div className="absolute inset-0 w-full h-full z-10">
        <div className="absolute left-4/7 top-1/2 w-3/9 h-fit bg-neutral-900 border border-neutral-700 rounded-2xl -translate-y-1/2">
          <Outlet />
        </div>
        <div className="absolute top-1/2 left-1/8 w-4/9 h-3/7 space-y-3 z-7">
          <div className="px-2 py-3 flex space-x-1">
            <img src="/logo.png" width={110} height={55} />
            <p className="text-8xl text-neutral-200 font-bold">Draw Duel</p>
          </div>
          <div className="pb-10">
            <p className="text-neutral-200 text-3xl font-(font-family:--font-hero)">
              Outdraw your rivals
            </p>
            <p className="text-neutral-200 text-3xl font-(font-family:--font-hero)">
              One sketch, one guess, one duel at a time.
            </p>
          </div>
          <Button className="w-1/4 h-1/8 text-xl font-bold bg-sky-600 hover:bg-sky-700 hover:cursor-pointer">
            Explore more
          </Button>
        </div>
      </div>
    </div>
  );
};

export default AuthLayout;
