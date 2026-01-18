import type { Column } from "@/components/layout/Dashboard/types";
import { Button } from "@/components/ui/button";

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
  os: string;
  browser: string;
  location: string;
  status: string;
  action: string;
}

export const DeviceManagementColumns: Column<DeviceManagementRow>[] = [
  {
    key: "os",
    label: "Operating System",
    className: "text-md font-semibold text-neutral-200",
  },
  {
    key: "browser",
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
      const status = String(value);

      const colorMap: Record<string, string> = {
        Active: "text-emerald-400",
        "Current Session": "text-sky-400",
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
      if (row.status !== "Active") {
        return <span className="text-neutral-400">—</span>;
      }

      return (
        <Button
          variant="destructive"
          size="sm"
          onClick={() => {
            console.log("Revoke session:", row);
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
    os: "Linux",
    browser: "Chrome",
    location: "Netherlands",
    status: "Current Session",
    action: "—",
  },
  {
    os: "Windows 11",
    browser: "Firefox",
    location: "Germany",
    status: "Active",
    action: "Revoke",
  },
  {
    os: "Android 14",
    browser: "Chrome",
    location: "Bulgaria",
    status: "Revoked",
    action: "—",
  },
  {
    os: "macOS Sonoma",
    browser: "Safari",
    location: "France",
    status: "Active",
    action: "Revoke",
  },
  {
    os: "iPadOS 17",
    browser: "Safari",
    location: "Italy",
    status: "Revoked",
    action: "—",
  },
  {
    os: "Windows 10",
    browser: "Edge",
    location: "United Kingdom",
    status: "Active",
    action: "Revoke",
  },
  {
    os: "iOS 17",
    browser: "Safari",
    location: "Spain",
    status: "Revoked",
    action: "—",
  },
  {
    os: "Ubuntu 22.04",
    browser: "Firefox",
    location: "Sweden",
    status: "Active",
    action: "Revoke",
  },
  {
    os: "Arch Linux",
    browser: "Brave",
    location: "Poland",
    status: "Active",
    action: "Revoke",
  },
  {
    os: "Android 13",
    browser: "Samsung Internet",
    location: "Romania",
    status: "Revoked",
    action: "—",
  },
];
