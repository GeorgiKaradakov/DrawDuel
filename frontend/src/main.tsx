import { createRoot } from "react-dom/client";
import "./styles/tailwind.css";
import { RouterProvider } from "react-router";
import { router } from "./lib/router.ts";

createRoot(document.getElementById("root")!).render(
  <RouterProvider router={router} />,
);
