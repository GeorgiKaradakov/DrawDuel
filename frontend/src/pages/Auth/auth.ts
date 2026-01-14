import { setAccessToken } from "@/lib/api";
import { api } from "@/lib/axios";

export const authApi = () => {
  const apiLogin = async (identifier: string, pass: string) => {
    try {
      const response = await api.post("/api/auth/login", {
        identifier,
        password: pass,
      });

      console.log(response);

      const { accessToken } = response.data;
      setAccessToken(accessToken);
      return accessToken;
    } catch (error: any) {
      const message = error.response?.data || "Login failed";
      throw new Error(message);
    }
  };

  const apiRegister = async (
    username: string,
    email: string,
    pass: string,
    passRepeat: string,
  ) => {
    try {
      const response = await api.post("/api/auth/register", {
        username,
        email,
        pass: pass,
        repeatPass: passRepeat,
      });

      const { accessToken } = response.data;
      setAccessToken(accessToken);
      return accessToken;
    } catch (error: any) {
      if (error.response?.data?.errors) {
        throw error.response.data.errors; // { email: "...", username: "..." }
      }

      const message =
        error.response?.data || "Registration failed. Please try again.";
      throw new Error(message);
    }
  };

  const apiLogout = async () => {
    await api.post("/api/auth/logout");
    setAccessToken("");
  };

  return { apiLogin, apiRegister, apiLogout };
};
