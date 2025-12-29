import AuthLayout from "@/components/layout/Auth/AuthLayout";
import Login from "@/pages/Auth/Login";
import Register from "@/pages/Auth/Register";
import Dashboard from "@/pages/Dashboard/Dashboard";
import FindGame from "@/pages/FindGame/FindGame";
import GameDraw from "@/pages/Game/GameDraw";
import GameGuess from "@/pages/Game/GameGuess";
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
      {
        path: "dashboard",
        Component: Dashboard,
      },
      {
        path: "game/someGameIdWhichWillBeProvidedLater",
        children: [{ path: "draw", Component: GameDraw }],
      },
      {
        path: "game/someGameIdWhichWillBeProvidedLater",
        children: [{ path: "guess", Component: GameGuess }],
      },
      {
        path: "find-game/someUserIdWhichWillBeProvidedLater",
        Component: FindGame,
      },
    ],
  },
]);
