import { motion } from "framer-motion";

type PresentWinnerProps = {
  playerAName: string;
  playerBName: string;
  playerAScore: number;
  playerBScore: number;
  winner: string; // username or "Draw"
  onPlayAgain: () => void;
  onReturnToDashboard: () => void;
};

const PresentWinner = ({
  playerAName,
  playerBName,
  playerAScore,
  playerBScore,
  winner,
  onPlayAgain,
  onReturnToDashboard,
}: PresentWinnerProps) => {
  const isDraw = winner === "Draw";

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
        className="p-10 bg-neutral-900/95 rounded-2xl border border-neutral-700 shadow-2xl text-center max-w-md w-full"
      >
        <h2 className="text-4xl font-extrabold text-neutral-50 mb-6">
          {isDraw ? "It's a Draw!" : "🏆 Winner"}
        </h2>

        {!isDraw && (
          <p className="text-3xl font-bold text-emerald-400 mb-6">{winner}</p>
        )}

        <div className="space-y-3 text-2xl mb-8">
          <p className="text-neutral-200">
            <span className="text-purple-400 font-bold">{playerAName}</span> —{" "}
            <span className="font-semibold">{playerAScore}</span> pts
          </p>
          <p className="text-neutral-200">
            <span className="text-blue-400 font-bold">{playerBName}</span> —{" "}
            <span className="font-semibold">{playerBScore}</span> pts
          </p>
        </div>

        <div className="flex gap-4 justify-center">
          <button
            onClick={onPlayAgain}
            className="px-6 py-3 rounded-xl bg-emerald-500 hover:bg-emerald-600 text-black font-bold transition"
          >
            Play Again
          </button>

          <button
            onClick={onReturnToDashboard}
            className="px-6 py-3 rounded-xl bg-neutral-700 hover:bg-neutral-600 text-neutral-200 font-semibold transition"
          >
            Dashboard
          </button>
        </div>
      </motion.div>
    </motion.div>
  );
};

export default PresentWinner;
