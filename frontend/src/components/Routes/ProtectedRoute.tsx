import { useAuth } from "@/context/authProvider/useAuth";
import { useEffect } from "react";
import { Navigate, Outlet } from "react-router";

export default function ProtectedRoute() {
  const { isAuthenticated, loading, initAuth } = useAuth();

  useEffect(() => {
    if (!loading && !isAuthenticated) initAuth();
  }, []);

  if (loading) return null;

  if (!isAuthenticated) {
    return <Navigate to="/auth/login" replace />;
  }

  return <Outlet />;
}
