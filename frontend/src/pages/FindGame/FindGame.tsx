import { Button } from "@/components/ui/button";
import Navbar from "@/components/layout/General/SideBar/Navbar";
import FindingAnimation from "@/components/layout/FindGame/FindingAnimation";
import { useEffect, useState } from "react";
import { useParams } from "react-router";
import { connectSocket } from "@/lib/webscoket/socketManager";
import { searchForGame } from "@/lib/webscoket/gameSocket";

const FindGame = () => {
  const { userId } = useParams();
  const [searching, setSearching] = useState(false);

  useEffect(() => {
    connectSocket();
  }, []);

  const handleFindGame = () => {
    if (!userId) return;
    searchForGame(userId);
    setSearching(true);
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
