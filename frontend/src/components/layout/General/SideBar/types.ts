import type { ReactNode } from "react";

export interface SectionProps {
  className?: string;
  sectionName: string;
  children?: React.ReactNode;
}

export interface SectionContentProps {
  className?: string;
  name: string;
  icon?: ReactNode;
  data_cy?: string;
  handleOnClick: () => void;
}

export interface AccountSectionProps {
  className?: string;
}
