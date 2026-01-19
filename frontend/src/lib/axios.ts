import axios from "axios";
import { getAccessToken, setAccessToken } from "./api";

export const api = axios.create({
  baseURL: import.meta.env.API_URL || "http://localhost:8080",
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

export const apiWithImage = axios.create({
  baseURL: import.meta.env.API_URL || "http://localhost:8080",
  withCredentials: true,
});

const refreshClient = axios.create({
  baseURL: import.meta.env.API_URL || "http://localhost:8080",
  withCredentials: true,
  headers: { "Content-Type": "application/json" },
});

apiWithImage.interceptors.request.use(
  (config) => {
    const token = getAccessToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

api.interceptors.request.use(
  (config) => {
    const token = getAccessToken();
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error),
);

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (
      error.response &&
      error.response.status === 401 &&
      !originalRequest._retry
    ) {
      originalRequest._retry = true;

      try {
        const res = await refreshClient.post("/api/auth/refresh-tokens");
        const { accessToken } = res.data;

        setAccessToken(accessToken);

        originalRequest.headers["Authorization"] = `Bearer ${accessToken}`;
        return api(originalRequest);
      } catch (error: any) {
        window.location.href = "/auth/login";
        return Promise.reject(error);
      }
    }

    return Promise.reject(error);
  },
);
