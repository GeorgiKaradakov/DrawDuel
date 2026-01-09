import { useEffect, useState } from "react";

const FindingAnimation = () => {
  const [dots, setDots] = useState(".");

  useEffect(() => {
    const interval = setInterval(() => {
      setDots((prev) => (prev.length < 3 ? prev + "." : "."));
    }, 500);

    return () => clearInterval(interval);
  }, []);

  return (
    <div className="fixed inset-0 flex flex-col items-center justify-center bg-black/70 backdrop-blur-sm z-50">
      <div className="w-16 h-16 border-4 border-violet-500 border-t-transparent rounded-full animate-spin mb-6"></div>

      <p className="text-2xl text-neutral-200 font-semibold">
        Searching for players{dots}
      </p>
    </div>
  );
};

export default FindingAnimation;
