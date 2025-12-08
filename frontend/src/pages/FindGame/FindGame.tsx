import FindingAnimation from "@/components/layout/FindGame/FindingAnimation";
import { Button } from "@/components/ui/button";
import { searchForGame } from "@/lib/webscoket/gameSocket";
import { connectSocket } from "@/lib/webscoket/socketManager";
import type { ServerMessage } from "@/lib/webscoket/types";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router";

const FindGame = () => {
  const [searching, setSearching] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    connectSocket(
      (data: ServerMessage) => {
        if (data.type === "waiting") setSearching(true);
        if (data.type === "start") {
          setSearching(false);
          navigate("/game/someGameIdWhichWillBeProvidedLater/draw");
        }
      },
      undefined,
      () => setSearching(false),
    );
  }, [navigate]);

  const handleFindGame = () => {
    searchForGame();
    setSearching(true);
  };

  return (
    <>
      <div className="w-screen h-screen flex justify-center items-center bg-neutral-600">
        <Button className="w-1/5 h-1/9 text-4xl" onClick={handleFindGame}>
          Find Game
        </Button>
      </div>
      {searching && <FindingAnimation />}
    </>
  );
};

export default FindGame;
