import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./styles/tailwind.css";
import { RouterProvider } from "react-router";
import { router } from "./lib/router.ts";
import { TooltipProvider } from "./components/ui/tooltip.tsx";

createRoot(document.getElementById("root")!).render(
  <RouterProvider router={router} />,
);
