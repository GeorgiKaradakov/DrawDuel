import { motion } from "framer-motion";
import type { PresentScoresProps } from "@/components/layout/Game/types";
import { useEffect, useState } from "react";

const PresentScores = ({ roundResults }: PresentScoresProps) => {
  const { drawerName, guesserName, drawerScore, guesserScore } = roundResults;
  const [countDown, setCountdown] = useState(5);

  useEffect(() => {
    if (countDown == 0) return; // trigger the next round
    const timer = setInterval(() => {
      setCountdown((prev) => prev - 1);
    }, 1000);

    return () => {
      clearInterval(timer);
    };
  }, [countDown]);

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      transition={{ duration: 0.6 }}
      className="fixed inset-0 z-50 flex justify-center items-center backdrop-blur-md bg-black/60"
    >
      <motion.div
        initial={{ scale: 0.85, opacity: 0 }}
        animate={{ scale: 1, opacity: 1 }}
        transition={{ duration: 0.4 }}
        className="p-8 bg-neutral-900/95 rounded-2xl border border-neutral-700 shadow-2xl text-center max-w-md"
      >
        <h2 className="text-4xl font-extrabold text-neutral-50 mb-6">
          Round Summary
        </h2>

        <div className="space-y-3 text-2xl">
          <p className="text-neutral-200">
            <span className="text-purple-400 font-bold">{drawerName}</span> —{" "}
            <span className="font-semibold">{drawerScore}</span> pts
          </p>
          <p className="text-neutral-200">
            <span className="text-blue-400 font-bold">{guesserName}</span> —{" "}
            <span className="font-semibold">{guesserScore}</span> pts
          </p>
        </div>

        <p className="mt-8 text-neutral-400 text-lg italic">
          Next round starting in {countDown}...
        </p>
      </motion.div>
    </motion.div>
  );
};

export default PresentScores;
