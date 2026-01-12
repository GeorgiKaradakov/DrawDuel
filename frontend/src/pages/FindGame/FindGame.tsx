import FindingAnimation from "@/components/layout/FindGame/FindingAnimation";
import Navbar from "@/components/layout/General/SideBar/Navbar";
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
    connectSocket(
      () => console.log("Socket opened for matchmaking"),
      () => setSearching(false),
    );

    const handleMessage = (data: ServerMessage) => {
      if (data.type === "waiting") {
        setSearching(true);
      }

      if (data.type === "sendToDraw") {
        navigate(`/game/${data.gameId}/draw`);
      }

      if (data.type === "wordChoice" && data.role === "drawer") {
        console.log("📝 [FindGame] WORD CHOICE RECEIVED");

        navigate(`/game/${data.gameId}/draw`, {
          state: {
            words: data.words,
            wordPickCountDown: data.countdown,
          },
        });
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
      <div className="w-screen h-screen flex bg-neutral-700">
        <Navbar />
        <div className="w-full h-full flex justify-center items-center">
          <Button className="w-1/5 h-1/9 text-4xl" onClick={handleFindGame}>
            Find Game
          </Button>
        </div>
      </div>
      {searching && <FindingAnimation />}
    </>
  );
};

export default FindGame;
