import { setAccessToken } from "@/lib/api";
import { api, apiWithImage } from "@/lib/axios";

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
    profileImage?: File,
  ) => {
    try {
      const formData = new FormData();

      formData.append("username", username);
      formData.append("email", email);
      formData.append("pass", pass);
      formData.append("repeatPass", passRepeat);

      if (profileImage) {
        formData.append("profileImage", profileImage);
      }

      const response = await apiWithImage.post("/api/auth/register", formData);

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
