import AuthLayout from "@/components/layout/Auth/AuthLayout";
import Login from "@/pages/Auth/Login";
import Register from "@/pages/Auth/Register";
import { createBrowserRouter } from "react-router";

export const router = createBrowserRouter([
  {
    path: "/",
    children: [
      {
        path: "auth",
        Component: AuthLayout,
        children: [
          { path: "login", Component: Login },
          { path: "register", Component: Register },
        ],
      },
    ],
  },
]);
