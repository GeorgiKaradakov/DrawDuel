export type DashboardCardType = {
  type: "number" | "table" | "chart";
  className?: string;
  title: string;
  number?: number;
  tableData?: any[];
  tableColumns?: Column<any>[];
  tableHeight?: string;
  chartData?: { day: string; games: number }[];
  percantege?: boolean;
};

export interface Column<T> {
  key: keyof T;
  label: string;
  className?: string;
  render?: (value: T[keyof T], row: T) => React.ReactNode;
}

export interface GenericTableProps<T> {
  title?: string;
  caption?: string;
  data: T[];
  columns: Column<T>[];
  className?: string;
}
