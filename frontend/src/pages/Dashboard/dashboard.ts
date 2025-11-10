import { api } from "@/lib/axios";

export const getDashboardData = async (): Promise<string> => {
  const res = await api.get("/api/dashboard/get");
  return res.data;
};
