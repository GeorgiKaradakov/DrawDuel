import { setAccessToken } from "@/lib/api";
import { api } from "@/lib/axios";

export const login = async (identifier: string, pass: string) => {
  try {
    const response = await api.post("/api/auth/login", {
      identifier,
      password: pass,
    });

    const { accessToken } = response.data;
    console.log(accessToken);
    setAccessToken(accessToken);
    return accessToken;
  } catch (error: any) {
    const message = error.response?.data?.message || "Login failed";
    throw new Error(message);
  }
};

export const register = async (
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
      error.response?.data?.message || "Registration failed. Please try again.";
    throw new Error(message);
  }
};
