import { api } from "@/lib/axios";

export const getDashboardData = async (): Promise<string> => {
  const res = await api.get("/api/dashboard/get");
  console.log(res);
  return res.data;
};
