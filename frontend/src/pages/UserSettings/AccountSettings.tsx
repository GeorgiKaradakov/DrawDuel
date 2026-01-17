import SettingsSidebar from "@/components/layout/General/SideBar/SettingsSidebar";
import SecuritySettings from "./SecuritySettings";
import UserSettings from "./UserSettings";
import DeviceManagement from "./DeviceManagement";

const AccountSettings = () => {
  return (
    <div className="flex w-full h-full">
      <SettingsSidebar />
      <div className="mt-10 w-full h-full flex flex-col items-center">
        <UserSettings />
        <SecuritySettings />
        <DeviceManagement />
      </div>
    </div>
  );
};

export default AccountSettings;
