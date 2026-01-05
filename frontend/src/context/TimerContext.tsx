import React, { createContext, useContext, useState } from "react";

interface TimerContextValue {
  timeLeft: number;
  setTimeLeft: React.Dispatch<React.SetStateAction<number>>;
}

const TimerContext = createContext<TimerContextValue | undefined>(undefined);

export const TimerProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [timeLeft, setTimeLeft] = useState(60); // default 60 seconds

  return (
    <TimerContext.Provider value={{ timeLeft, setTimeLeft }}>
      {children}
    </TimerContext.Provider>
  );
};

export const useTimer = () => {
  const context = useContext(TimerContext);
  if (!context) throw new Error("useTimer must be used inside a TimerProvider");
  return context;
};
