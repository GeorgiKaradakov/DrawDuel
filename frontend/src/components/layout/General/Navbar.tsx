import { Button } from "@/components/ui/button";
import { getUserIdFromToken } from "@/lib/jwt";
import { useNavigate } from "react-router";

const Navbar = () => {
  const navigate = useNavigate();

  const handlePlayClick = () => {
    const token = localStorage.getItem("accessToken"); // stored after login
    const userId = getUserIdFromToken(token || "");

    if (userId) {
      navigate(`/find-game/${userId}`);
    } else {
      alert("Invalid or missing token!");
    }
  };

  return (
    <div className="w-[20%] h-full flex justify-center items-center border-r border-r-neutral-300">
      <Button className="text-lg w-30 h-20" onClick={handlePlayClick}>
        Play
      </Button>
    </div>
  );
};

export default Navbar;
