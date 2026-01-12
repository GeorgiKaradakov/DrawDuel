import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import Navbar from "@/components/layout/General/SideBar/Navbar";
import DashboardCard from "@/components/layout/Dashboard/components/DashboardCard";
import { getDashboardData } from "./dashboard";
import type { DashboardResponse } from "@/lib/globalTypes";

const leaderboardColumns = [
  { key: "rank", label: "Rank" },
  { key: "userName", label: "Username" },
  { key: "score", label: "Score" },
  { key: "wins", label: "Wins" },
];

const matchesColumns = [
  { key: "matchId", label: "Match ID" },
  { key: "drawPoints", label: "Draw Points" },
  { key: "guessPoints", label: "Guess Points" },
  { key: "outcome", label: "Outcome" },
];

const Dashboard = () => {
  const navigate = useNavigate();

  const [data, setData] = useState<DashboardResponse | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getDashboardData()
      .then(setData)
      .catch((err) => {
        console.error("Dashboard load failed:", err.message);
        setError("Failed to load dashboard");
        navigate("/auth/login");
      });
  }, [navigate]);

  if (error) {
    return (
      <div className="w-screen h-screen flex justify-center items-center bg-neutral-700 text-red-500 text-4xl font-bold">
        {error}
      </div>
    );
  }

  if (!data) {
    return (
      <div className="w-screen h-screen flex justify-center items-center bg-neutral-700 text-neutral-400 text-xl">
        Loading dashboard...
      </div>
    );
  }

  const { stats, leaderboard, recentMatches, chartData } = data;

  return (
    <div className="w-screen h-screen flex bg-neutral-700">
      <Navbar />

      <div className="w-full h-full grid grid-cols-4 grid-rows-5 p-4">
        {/* TOP SECTION */}
        <div className="col-span-4 row-span-3 flex justify-center items-center gap-x-6">
          <DashboardCard
            type="table"
            title="Leaderboard"
            tableData={leaderboard}
            tableColumns={leaderboardColumns}
            tableHeight="max-h-[85%]"
            className="w-[48%] h-[85%] rounded-2xl"
          />

          <div className="w-[48%] h-[85%] grid grid-cols-2 grid-rows-2 gap-6">
            <DashboardCard
              type="number"
              title="Total Matches"
              number={stats.totalMatches}
            />
            <DashboardCard
              type="number"
              title="Win / Loss Ratio"
              number={Number(stats.winLossRatio.toFixed(2))}
              percantege
            />
            <DashboardCard
              type="number"
              title="Total Wins"
              number={stats.totalWins}
            />
            <DashboardCard
              type="number"
              title="Total Losses"
              number={stats.totalLosses}
            />
          </div>
        </div>

        {/* BOTTOM SECTION */}
        <div className="col-span-2 row-span-3 px-4">
          <DashboardCard
            type="table"
            title="Recent Matches"
            tableData={recentMatches}
            tableColumns={matchesColumns}
            tableHeight="max-h-[82%]"
            className="rounded-2xl w-full h-[95%]"
          />
        </div>

        <div className="col-span-2 row-span-3 px-4">
          <DashboardCard
            type="chart"
            title="Games Played Per Day"
            chartData={chartData}
            className="rounded-2xl w-full h-[95%]"
          />
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
