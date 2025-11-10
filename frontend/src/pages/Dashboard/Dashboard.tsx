import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { getDashboardData } from "./dashboard";

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
    <div className="w-screen h-screen flex justify-center items-center bg-neutral-700 text-violet-500 text-5xl text-bold">
      {message}
    </div>
  );
};

export default Dashboard;
