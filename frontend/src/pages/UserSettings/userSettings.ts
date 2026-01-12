import { api } from "@/lib/axios";

export const updateProfile = async (username: string, email: string) => {
  await api.put("/api/user/profile", {
    username,
    email,
  });
};

export const getMyProfile = async () => {
  const res = await api.get("/api/user/me");
  return res.data as { username: string; email: string };
};

export const deleteAccountApi = async () => {
  await api.delete("/api/user");
};
