import { useState } from "react";

type Session = {
  id: string;
  userAgent: string;
  ipAdress: string;
  issuedAt: string;
  expiresAt: string;
  revoked: boolean;
};

const DeviceManagement = () => {
  const [sessions, setSessions] = useState<Session[]>([
    {
      id: "1",
      userAgent: "Chrome on Windows",
      ipAdress: "192.168.1.10",
      issuedAt: "2025-01-10 12:30",
      expiresAt: "2025-01-17 12:30",
      revoked: false,
    },
    {
      id: "2",
      userAgent: "Firefox on Linux",
      ipAdress: "192.168.1.22",
      issuedAt: "2025-01-05 09:15",
      expiresAt: "2025-01-12 09:15",
      revoked: true,
    },
  ]);

  const revokeSession = (id: string) => {
    setSessions((prev) =>
      prev.map((s) => (s.id === id ? { ...s, revoked: true } : s)),
    );
  };

  return (
    <div className="max-w-4xl mx-auto p-6">
      <h1 className="text-2xl font-bold mb-6">Devices & Sessions</h1>

      <table className="w-full border-collapse border">
        <thead>
          <tr className="bg-gray-100">
            <th className="border p-2">Device</th>
            <th className="border p-2">IP</th>
            <th className="border p-2">Issued</th>
            <th className="border p-2">Expires</th>
            <th className="border p-2">Status</th>
            <th className="border p-2"></th>
          </tr>
        </thead>

        <tbody>
          {sessions.map((s) => (
            <tr key={s.id}>
              <td className="border p-2">{s.userAgent}</td>
              <td className="border p-2">{s.ipAdress}</td>
              <td className="border p-2">{s.issuedAt}</td>
              <td className="border p-2">{s.expiresAt}</td>
              <td className="border p-2">
                {s.revoked ? (
                  <span className="text-red-600">Revoked</span>
                ) : (
                  <span className="text-green-600">Active</span>
                )}
              </td>
              <td className="border p-2">
                {!s.revoked && (
                  <button
                    onClick={() => revokeSession(s.id)}
                    className="text-red-600 hover:underline text-sm"
                  >
                    Revoke
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default DeviceManagement;
