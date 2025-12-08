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

  // if (!message) {
  //   return (
  //     <div className="flex justify-center items-center h-screen text-neutral-400 text-xl">
  //       Loading dashboard...
  //     </div>
  //   );
  // }

  return (
    <div className="w-screen h-screen flex bg-neutral-700">
      <Navbar />
      <div className="w-full h-full grid grid-cols-4 grid-rows-5 p-4">
        <div className="col-span-4 row-span-3 flex items-center justify-evenly gap-x-4">
          <DashboardCard
            type="table"
            title="Leaderboard"
            tableData={leaderboardData}
            tableColumns={leaderboardColumns}
            tableHeight="max-h-[85%]"
            className="col-span-2 rounded-2xl w-[48%] h-[85%]"
          />

          <div className=" w-[48%] h-[85%] col-span-2 grid grid-cols-2 grid-rows-2 gap-6">
            <DashboardCard
              type="number"
              title="Total matches"
              number={12}
              className="rounded-2xl"
            />

            <DashboardCard
              type="number"
              title="Win/Loss ratio"
              number={0.5}
              percantege={true}
              className="rounded-2xl"
            />

            <DashboardCard
              type="number"
              title="Total Wins"
              number={6}
              className="rounded-2xl"
            />

            <DashboardCard
              type="number"
              title="Total losses"
              number={6}
              className="rounded-2xl"
            />
          </div>
        </div>

        <div className="col-span-2 row-span-3 flex justify-center items-center px-4">
          <DashboardCard
            type="table"
            title="Recent Matches"
            tableData={Matches}
            tableColumns={MatchesColumns}
            tableHeight="max-h-[82%]"
            className="rounded-2xl w-full h-[95%]"
          />
        </div>

        <div className="col-span-2 row-span-3 flex justify-center items-center px-4">
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
