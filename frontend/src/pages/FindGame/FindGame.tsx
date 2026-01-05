import FindingAnimation from "@/components/layout/FindGame/FindingAnimation";
import { Button } from "@/components/ui/button";
import { searchForGame } from "@/lib/webscoket/gameSocket";
import {
  addSocketListener,
  connectSocket,
  removeSocketListener,
} from "@/lib/webscoket/socketManager";
import type { ServerMessage } from "@/lib/webscoket/types";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";

const FindGame = () => {
  const [searching, setSearching] = useState(false);
  const navigate = useNavigate();
  const { userId } = useParams();

  useEffect(() => {
    // 1️⃣ Ensure socket connection exists
    connectSocket(
      () => console.log("Socket opened for matchmaking"),
      () => setSearching(false),
    );

    // 2️⃣ Define a single listener for messages
    const handleMessage = (data: ServerMessage) => {
      if (data.type === "waiting") {
        setSearching(true);
      }

      if (data.type === "sendToDraw") {
        navigate(`/game/${data.gameId}/draw`);
      }

      if (data.type === "sendToGuess") {
        navigate(`/game/${data.gameId}/guess`);
      }
    };

    // 3️⃣ Add listener to the bus
    addSocketListener(handleMessage);

    // 4️⃣ Cleanup on unmount
    return () => {
      removeSocketListener(handleMessage);
    };
  }, [navigate]);

  const handleFindGame = () => {
    if (userId) {
      searchForGame(userId);
      setSearching(true);
    }
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
