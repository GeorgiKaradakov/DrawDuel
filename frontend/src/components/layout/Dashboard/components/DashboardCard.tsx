import { cn } from "@/lib/utils";
import type { DashboardCardType } from "../types";
import { GenericTable } from "./GenericTable";
import {
  CartesianGrid,
  Line,
  LineChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";

const DashboardCard = ({
  type,
  className,
  title,
  number,
  tableData,
  tableColumns,
  tableHeight,
  chartData,
  percantege,
}: DashboardCardType) => {
  switch (type) {
    case "number":
      return (
        <div
          className={cn(
            "bg-neutral-600 text-white space-y-11 px-3 py-3 rounded-lg shadow-md",
            className,
          )}
        >
          <h1 className="font-bold text-2xl">{title}</h1>
          <p className="font-semibold text-6xl text-center">
            {number}
            {percantege && <span className="text-4xl">%</span>}
          </p>
        </div>
      );

    case "table":
      return (
        <div
          className={cn(
            "bg-neutral-600 text-white space-y-6 px-3 py-2 rounded-lg shadow-md",
            className,
          )}
        >
          <h1 className="font-bold text-xl">{title}</h1>
          <GenericTable
            title={title}
            caption="Generated data overview"
            data={tableData || []}
            columns={tableColumns || []}
            className={tableHeight}
          />
        </div>
      );

    case "chart":
      return (
        <div
          className={cn(
            "bg-neutral-600 text-white px-3 py-4 rounded-lg shadow-md flex flex-col justify-center",
            className,
          )}
        >
          <h1 className="font-bold text-xl mb-4">{title}</h1>
          <div className="w-full h-full flex justify-center items-center">
            <ResponsiveContainer width="100%" height={250}>
              <LineChart
                data={chartData || []}
                margin={{ top: 10, right: 30, left: 0, bottom: 0 }}
              >
                <CartesianGrid strokeDasharray="3 3" stroke="#4b5563" />
                <XAxis dataKey="day" stroke="#d1d5db" />
                <YAxis stroke="#d1d5db" />
                <Tooltip
                  contentStyle={{
                    backgroundColor: "#1f2937",
                    border: "1px solid #4b5563",
                    color: "#fff",
                  }}
                />
                <Line
                  type="monotone"
                  dataKey="games"
                  stroke="#8b5cf6"
                  strokeWidth={2}
                  dot={{ r: 4 }}
                  activeDot={{ r: 6 }}
                />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </div>
      );

    default:
      return null;
  }
};

export default DashboardCard;
