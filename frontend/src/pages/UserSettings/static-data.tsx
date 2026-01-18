import type { Column } from "@/components/layout/Dashboard/types";
import { Button } from "@/components/ui/button";
import { revokeDevice } from "./server";

export const userProperties = [
  {
    title: "Profile Image",
    content: (content: string) => content,
    isImage: true,
  },
  { title: "Username", content: (content: string) => content, isImage: false },
  { title: "Email", content: (content: string) => content, isImage: false },
];

export interface DeviceManagementRow {
  sessionId: string;
  osName: string;
  browserName: string;
  location: string;
  status: string;
  // action: string;
}

export const DeviceManagementColumns: Column<DeviceManagementRow>[] = [
  {
    key: "osName",
    label: "Operating System",
    className: "text-md font-semibold text-neutral-200",
  },
  {
    key: "browserName",
    label: "Browser",
    className: "text-md font-semibold text-neutral-200",
  },
  {
    key: "location",
    label: "Location",
    className: "text-md font-semibold text-neutral-200",
  },
  {
    key: "status",
    label: "Status",
    className: "text-md font-semibold",
    render: (value) => {
      const status = String(value).toLowerCase().replace("_", " ");

      const colorMap: Record<string, string> = {
        active: "text-emerald-400",
        "current session": "text-sky-400",
      };

      return (
        <span className={colorMap[status] ?? "text-neutral-300"}>{status}</span>
      );
    },
  },
  {
    key: "action",
    label: "Action",
    className: "text-md font-semibold text-neutral-200",
    render: (_, row) => {
      if (!["ACTIVE", "CURRENT_SESSION"].includes(row.status)) {
        return <span className="text-neutral-400">—</span>;
      }

      return (
        <Button
          variant="destructive"
          size="sm"
          onClick={async () => {
            await revokeDevice(row.sessionId).catch((error) => {
              console.log(error);
            });
          }}
        >
          Revoke
        </Button>
      );
    },
  },
];

export const DeviceManagementDummyData: DeviceManagementRow[] = [
  {
    osName: "Linux",
    browserName: "Chrome",
    location: "Netherlands",
    status: "Current Session",
    action: "Revoke",
  },
  {
    osName: "Windows 11",
    browserName: "Firefox",
    location: "Germany",
    status: "Active",
    action: "Revoke",
  },
  {
    osName: "Android 14",
    browserName: "Chrome",
    location: "Bulgaria",
    status: "Revoked",
    action: "—",
  },
  {
    osName: "macOS Sonoma",
    browserName: "Safari",
    location: "France",
    status: "Active",
    action: "Revoke",
  },
  {
    osName: "iPadOS 17",
    browserName: "Safari",
    location: "Italy",
    status: "Revoked",
    action: "—",
  },
  {
    osName: "Windows 10",
    browserName: "Edge",
    location: "United Kingdom",
    status: "Active",
    action: "Revoke",
  },
  {
    osName: "iOS 17",
    browserName: "Safari",
    location: "Spain",
    status: "Revoked",
    action: "—",
  },
  {
    osName: "Ubuntu 22.04",
    browserName: "Firefox",
    location: "Sweden",
    status: "Active",
    action: "Revoke",
  },
  {
    osName: "Arch Linux",
    browserName: "Brave",
    location: "Poland",
    status: "Active",
    action: "Revoke",
  },
  {
    osName: "Android 13",
    browserName: "Samsung Internet",
    location: "Romania",
    status: "Revoked",
    action: "—",
  },
];
