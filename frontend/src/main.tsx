import { createRoot } from "react-dom/client";
import "./styles/tailwind.css";
import { RouterProvider } from "react-router";
import { router } from "./lib/router.ts";
import AuthProvider from "./context/authProvider/AuthProvider.tsx";

createRoot(document.getElementById("root")!).render(
  <AuthProvider>
    <RouterProvider router={router} />
  </AuthProvider>,
);
