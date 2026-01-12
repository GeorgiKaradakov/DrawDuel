export type SectionId = "GamePlay" | "UserSettings" | "Analytics";

export interface SectionContentItem {
  id: string;
  name: string;
  url: string | ((userId: string) => string);
}

export const sections = [
  { id: "Analytics" as SectionId, sectionName: "Overview" },
  { id: "GamePlay" as SectionId, sectionName: "Game Play" },
  { id: "UserSettings" as SectionId, sectionName: "User Settings" },
];

export const sectionContents: Record<SectionId, SectionContentItem[]> = {
  Analytics: [{ id: "ViewStats", name: "Dashboard", url: "/dashboard" }],
  GamePlay: [
    {
      id: "FindMatch",
      name: "Find Game",
      url: (userId: string) => `/find-game/${userId}`,
    },
  ],
  UserSettings: [
    {
      id: "UpdateProfile",
      name: "Update Profile",
      url: (userId: string) => `/user-settings/update-profile/${userId}`,
    },
    {
      id: "DeviceManagement",
      name: "Device Management",
      url: (userId: string) => `/user-settings/device-management/${userId}`,
    },
  ],
} as const;
