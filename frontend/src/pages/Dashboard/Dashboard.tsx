import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { getDashboardData } from "./dashboard";
import DashboardCard from "@/components/layout/Dashboard/components/DashboardCard";
import Navbar from "@/components/layout/General/Navbar";
import {
  chartData,
  leaderboardColumns,
  leaderboardData,
  Matches,
  MatchesColumns,
} from "./temp-data";

const Dashboard = () => {
  const [message, setMessage] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const text = await getDashboardData();
        setMessage(text);
      } catch (err) {
        // If error occurs (e.g., 401 Unauthorized), redirect to login
        setError(error);
      }
    };

    fetchData();
  }, [navigate]);

  if (error) {
    return (
      <div className="w-screen h-screen flex justify-center items-center bg-neutral-700 text-red-500 text-5xl text-bold">
        {error}
      </div>
    );
  }

  if (!message) {
    return (
      <div className="flex justify-center items-center h-screen text-neutral-400 text-xl">
        Loading dashboard...
      </div>
    );
  }

  return (
    <div className="w-screen h-screen flex bg-neutral-700">
      <Navbar />
      <div className="w-full h-full grid grid-cols-4 grid-rows-5 p-4">
        {/* === TOP SECTION (Centered Leaderboard + Stats) === */}
        <div className="col-span-4 flex items-center justify-evenly row-span-2">
          <DashboardCard
            type="table"
            title="Leaderboard"
            tableData={leaderboardData}
            tableColumns={leaderboardColumns}
            className="rounded-2xl w-1/3 h-[85%]"
          />

          <DashboardCard
            type="number"
            title="Total matches"
            number={12}
            className="rounded-2xl w-1/4 h-[70%]"
          />

          <DashboardCard
            type="number"
            title="Total wins"
            number={6}
            className="rounded-2xl w-1/4 h-[70%]"
          />
        </div>

        {/* === BOTTOM SECTION (Tables) === */}
        <div className="col-span-2 row-span-3 flex justify-center items-center">
          <DashboardCard
            type="table"
            title="Recent Matches"
            tableData={Matches}
            tableColumns={MatchesColumns}
            tableHeight="max-h-110"
            className="rounded-2xl w-[95%] h-[95%]"
          />
        </div>

        <div className="col-span-2 row-span-3 flex justify-center items-center">
          <DashboardCard
            type="chart"
            title="Games Played Per Day"
            chartData={chartData}
            className="rounded-2xl w-[95%] h-[95%]"
          />
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
