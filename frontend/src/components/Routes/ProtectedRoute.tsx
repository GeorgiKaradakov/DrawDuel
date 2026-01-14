import { useAuth } from "@/context/authProvider/useAuth";
import { Navigate, Outlet } from "react-router";

export default function ProtectedRoute() {
  const { isAuthenticated, loading } = useAuth();

  if (loading) return null;

  if (!isAuthenticated) {
    return <Navigate to="/auth/login" replace />;
  }

  return <Outlet />;
}
