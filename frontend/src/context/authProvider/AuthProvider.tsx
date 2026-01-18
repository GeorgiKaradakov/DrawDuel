import { useEffect, useState } from "react";
import { type AuthUser, type AuthProviderProps } from "./types";
import { authApi } from "@/pages/Auth/auth";
import { api } from "@/lib/axios";
import { AuthContext } from "./AuthContext";

const AuthProvider = ({ children }: AuthProviderProps) => {
  const { apiLogin, apiRegister, apiLogout } = authApi();

  const [user, setUser] = useState<AuthUser | null>(null);
  const [loading, setLoading] = useState(false);

  const initAuth = async () => {
    setLoading(true);
    try {
      const res = await api.get<AuthUser>("/api/auth/me");
      setUser(res.data);
    } catch {
      setUser(null);
    } finally {
      setLoading(false);
    }
  };

  const login = async (identifier: string, password: string) => {
    await apiLogin(identifier, password);
    const res = await api.get<AuthUser>("/api/auth/me");
    setUser(res.data);
  };

  const register = async (
    username: string,
    email: string,
    password: string,
    passwordRepeat: string,
    profileImage?: File,
  ) => {
    await apiRegister(username, email, password, passwordRepeat, profileImage);
    const res = await api.get<AuthUser>("/api/auth/me");
    setUser(res.data);
  };

  const logout = async () => {
    try {
      await apiLogout();
    } finally {
      setUser(null);
      window.location.href = "/auth/login";
    }
  };

  // useEffect(() => {
  //   initAuth();
  // }, []);

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated: !!user,
        loading,
        login,
        register,
        logout,
        setUser,
        initAuth,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export default AuthProvider;
