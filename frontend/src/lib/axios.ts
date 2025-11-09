import axios from "axios";
import { getAccessToken, setAccessToken } from "./api";

export const api = axios.create({
  baseURL: import.meta.env.API_URL || "http://localhost:8080",
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

const refreshClient = axios.create({
  baseURL: import.meta.env.API_URL || "http://localhost:8080",
  withCredentials: true,
  headers: { "Content-Type": "application/json" },
});

let isRefreshing = false;
let refreshTokens: ((token: string) => void)[] = [];

function TokenRefresh(ch: (toekn: string) => void) {
  refreshTokens.push(ch);
}

function onRefreshed(token: string) {
  refreshTokens.forEach((ch) => ch(token));
  refreshTokens = [];
}

api.interceptors.request.use(
  (config) => {
    const token = getAccessToken();
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
      console.log("Attached token");
    } else console.log("⚠️ No token found");
    return config;
  },
  (error) => Promise.reject(error),
);

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      if (isRefreshing) {
        return new Promise((resolve) => {
          TokenRefresh((token) => {
            originalRequest.headers = {
              ...originalRequest.headers,
              Authorization: `Bearer ${token}`,
            };
            resolve(api(originalRequest));
          });
        });
      }

      originalRequest._retry = true;
      isRefreshing = true;

      try {
        const res = await refreshClient.post("/api/auth/refresh-tokens");
        const newToken = res.data.access_token;
        setAccessToken(newToken);
        onRefreshed(newToken);
        originalRequest.headers = {
          ...originalRequest.headers,
          Authorization: `Bearer ${newToken}`,
        };
        console.log("🔄 Retrying request with new token:", newToken);
        return api(originalRequest);
      } catch (err) {
        isRefreshing = false;
        console.error("Token refresh failed", err);
        window.location.href = "/auth/login";
        return Promise.reject(err);
      }
    }

    return Promise.reject(error);
  },
);
