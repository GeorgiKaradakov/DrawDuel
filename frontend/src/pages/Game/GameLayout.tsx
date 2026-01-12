import { Outlet } from "react-router";

const GameLayout = () => {
  return (
    <div className="w-screen h-screen flex">
      <Outlet />
    </div>
  );
};

export default GameLayout;
