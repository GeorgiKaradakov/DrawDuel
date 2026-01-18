import AuthLayout from "@/components/layout/Auth/AuthLayout";
import SettingsLayout from "@/components/layout/Settings/SettingsLayout";
import ProtectedRoute from "@/components/Routes/ProtectedRoute";
import PublicRoute from "@/components/Routes/PublicRoute";
import GameProvider from "@/context/gameContextProvider/GameProvider";

import Login from "@/pages/Auth/Login";
import Register from "@/pages/Auth/Register";
import Dashboard from "@/pages/Dashboard/Dashboard";
import FindGame from "@/pages/FindGame/FindGame";
import GameDraw from "@/pages/Game/GameDraw";
import GameGuess from "@/pages/Game/GameGuess";
import AccountSettings from "@/pages/UserSettings/AccountSettings";

import { createBrowserRouter } from "react-router";

export const router = createBrowserRouter([
  {
    path: "/",
    children: [
      {
        Component: PublicRoute,
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

      {
        Component: GameProvider,
        children: [
          {
            Component: ProtectedRoute,
            children: [
              {
                path: "dashboard",
                Component: Dashboard,
              },

              {
                path: "find-game/:userId",
                Component: FindGame,
              },

              {
                path: "game/:gameId",
                children: [
                  { path: "draw", Component: GameDraw },
                  { path: "guess", Component: GameGuess },
                ],
              },

              {
                path: "user-settings",
                Component: SettingsLayout,
                children: [
                  {
                    path: "account/:userId",
                    Component: AccountSettings,
                  },
                ],
              },
            ],
          },
        ],
      },
    ],
  },
]);
