import { useCallback, useEffect, useState } from "react";
import { type AuthUser, type AuthProviderProps } from "./types";
import { jwtDecode } from "jwt-decode";
import { getAccessToken } from "@/lib/api";
import { authApi } from "@/pages/Auth/auth";
import { AuthContext } from "./AuthContext";

const AuthProvider = ({ children }: AuthProviderProps) => {
  const { apiLogin, apiRegister, apiLogout } = authApi();

  const [user, setUser] = useState<AuthUser | null>(null);
  const [loading, setLoading] = useState(true);

  const decodeAndSetUser = useCallback((token: string) => {
    const decoded = jwtDecode<AuthUser>(token);
    setUser(decoded);
  }, []);

  const isTokenValid = (token: string) => {
    try {
      const decoded = jwtDecode<AuthUser>(token);
      return Date.now() < decoded.exp * 1000;
    } catch {
      return false;
    }
  };

  useEffect(() => {
    const token = getAccessToken();

    if (token && isTokenValid(token)) {
      decodeAndSetUser(token);
    } else {
      setUser(null);
    }

    setLoading(false);
  }, [decodeAndSetUser]);

  // 🔐 login uses YOUR function
  const login = async (identifier: string, password: string) => {
    const token = await apiLogin(identifier, password);
    decodeAndSetUser(token);
  };

  // 📝 register uses YOUR function (auto-login)
  const register = async (
    username: string,
    email: string,
    password: string,
    passwordRepeat: string,
  ) => {
    const token = await apiRegister(username, email, password, passwordRepeat);
    decodeAndSetUser(token);
  };

  const logout = async () => {
    try {
      await apiLogout();
    } finally {
      setUser(null);
      window.location.href = "/auth/login";
    }
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated: !!user,
        loading,
        login,
        register,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export default AuthProvider;
