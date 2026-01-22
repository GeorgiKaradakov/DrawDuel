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
  sessionId: string;
  osName: string;
  browserName: string;
  location: string;
  status: string;
  action: string;
}

export const DeviceManagementColumns = (
  onRevoke: (sessionId: string) => Promise<void>,
  revokingSessionId: string | null,
): Column<DeviceManagementRow>[] => [
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

      const isCurrentSession = row.status === "CURRENT_SESSION";
      const isLoading = revokingSessionId === row.sessionId;
      const disableAll = revokingSessionId !== null;

      return (
        <Button
          variant="destructive"
          size="sm"
          disabled={disableAll}
          className="min-w-[90px]"
          data-cy={
            isCurrentSession
              ? "revoke-session-button-current-session"
              : "revoke-session-button"
          }
          onClick={() => onRevoke(row.sessionId)}
        >
          {isLoading ? <span className="animate-spin">⏳</span> : "Revoke"}
        </Button>
      );
    },
  },
];
