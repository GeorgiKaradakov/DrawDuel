import { useEffect, useState } from "react";
import {
  DeviceManagementColumns,
  type DeviceManagementRow,
} from "./static-data.tsx";
import { GenericTable } from "@/components/layout/Dashboard/components/GenericTable";
import { getDevicesData, revokeDevice } from "./server.ts";
import { errorToast, successToast } from "@/lib/toast.ts";

const DeviceManagement = () => {
  const [devicesData, setDevicesData] = useState<DeviceManagementRow[]>([]);
  const [revokingSessionId, setRevokingSessionId] = useState<string | null>(
    null,
  );

  const handleRevoke = async (sessionId: string) => {
    if (revokingSessionId !== null) return;

    try {
      setRevokingSessionId(sessionId);
      await revokeDevice(sessionId)
        .then(() => {
          setDevicesData((prev) =>
            prev.filter((d) => d.sessionId !== sessionId),
          );
          successToast("Device session revoked");
        })
        .catch((error) => {
          errorToast(error.response?.data || "Failed to revoke device");
        });
    } catch (e) {
      console.error(e);
    } finally {
      setRevokingSessionId(null);
    }
  };

  useEffect(() => {
    getDevicesData()
      .then((response) => {
        setDevicesData(response.data.devices);
      })
      .catch((error) => {
        console.log(error);
      });
  }, []);

  return (
    <div className="mb-20 w-7/8 h-full space-y-4">
      <h1 className="text-3xl font-bold text-neutral-50">Devices</h1>
      <GenericTable
        className="max-h-160"
        tableHeaderClassName="hover:bg-neutral-600"
        data={devicesData}
        columns={DeviceManagementColumns(handleRevoke, revokingSessionId)}
      />
    </div>
  );
};

export default DeviceManagement;
