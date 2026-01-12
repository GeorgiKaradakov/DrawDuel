import { Outlet } from "react-router";
import Navbar from "../General/SideBar/Navbar";

const SettingsLayout = () => {
  return (
    <div className="w-creen h-screen bg-neutral-700 flex">
      <Navbar />
      <div className="w-full h-full">
        <Outlet />
      </div>
    </div>
  );
};

export default SettingsLayout;
