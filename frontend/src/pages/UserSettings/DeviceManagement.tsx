import {
  DeviceManagementColumns,
  DeviceManagementDummyData,
} from "./static-data.tsx";
import { GenericTable } from "@/components/layout/Dashboard/components/GenericTable";

const DeviceManagement = () => {
  return (
    <div className="mb-20 w-7/8 h-full space-y-4">
      <h1 className="text-3xl font-bold text-neutral-50">Devices</h1>
      <GenericTable
        className="max-h-160"
        tableHeaderClassName="hover:bg-neutral-600"
        data={DeviceManagementDummyData}
        columns={DeviceManagementColumns}
      />
    </div>
  );
};

export default DeviceManagement;
