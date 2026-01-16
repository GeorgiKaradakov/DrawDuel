import { Laptop, ShieldCheck, User, type LucideIcon } from "lucide-react";

export type SectionId = "GamePlay" | "UserSettings" | "Analytics";
type ProfileSectionId = "accountSettings" | "deviceManagement";

export interface SectionContentItem {
  id: string;
  name: string;
  url: string | ((userId: string) => string);
}

interface ProfileLinkItems {
  id: string;
  name: string;
  Icon: LucideIcon;
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
      id: "AccountSettings",
      name: "Account Settings",
      url: (userId: string) => `/user-settings/account/${userId}`,
    },
  ],
} as const;

export const profileSections = [
  { id: "accountSettings" as ProfileSectionId, name: "Account Settings" },
  { id: "deviceManagement" as ProfileSectionId, name: "Device Management" },
];

export const profileLinks: Record<ProfileSectionId, ProfileLinkItems[]> = {
  accountSettings: [
    { id: "account-settings", name: "Account", Icon: User },
    { id: "security-settings", name: "Security", Icon: ShieldCheck },
  ],
  deviceManagement: [
    { id: "device-management", name: "Devices", Icon: Laptop },
  ],
} as const;
