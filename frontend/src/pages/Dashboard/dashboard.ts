import { api } from "@/lib/axios";
import type { DashboardResponse } from "@/lib/globalTypes";

export const getDashboardData = async (): Promise<DashboardResponse> => {
  const res = await api.get<DashboardResponse>("/api/dashboard/get");
  return res.data;
};
