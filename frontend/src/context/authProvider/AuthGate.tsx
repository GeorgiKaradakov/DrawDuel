import { useEffect, useRef } from "react";
import { Navigate, Outlet } from "react-router";
import { useAuth } from "./useAuth";

const AuthGate = () => {
  const { initAuth, isAuthenticated, loading } = useAuth();
  const initialized = useRef(false);

  useEffect(() => {
    if (!initialized.current) {
      initAuth();
      initialized.current = true;
    }
  }, []);

  if (loading) return null;

  if (!isAuthenticated) {
    return <Navigate to="/auth/login" replace />;
  }

  return <Outlet />;
};

export default AuthGate;
