import SettingsSidebar from "@/components/layout/General/SideBar/SettingsSidebar";
import SecuritySettings from "./SecuritySettings";
import UserSettings from "./UserSettings";
import DeviceManagement from "./DeviceManagement";

const AccountSettings = () => {
  return (
    <div className="flex w-full h-full overflow-y-hidden">
      <SettingsSidebar />
      <div
        id="settings-scroll-container"
        className="mt-10 w-full h-full flex flex-col items-center space-y-50 overflow-y-auto"
      >
        <section
          id="account-settings"
          className="scroll-mt-24 flex justify-center items-center w-full"
        >
          <UserSettings />
        </section>

        <section
          id="security-settings"
          className="scroll-mt-24 flex justify-center items-center w-full"
        >
          <SecuritySettings />
        </section>

        <section
          id="device-management"
          className="scroll-mt-24 flex justify-center items-center w-full"
        >
          <DeviceManagement />
        </section>
      </div>
    </div>
  );
};

export default AccountSettings;
