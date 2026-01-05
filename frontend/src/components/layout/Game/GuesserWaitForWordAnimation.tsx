import { Loader2 } from "lucide-react";

const GuesserWaitForWordAnimation = () => {
  return (
    <div className="w-screen h-screen flex flex-col justify-center items-center bg-neutral-800 text-neutral-50 space-y-6">
      <Loader2 className="animate-spin w-16 h-16 text-purple-400" />
      <p className="text-3xl font-semibold">
        Waiting for drawer to choose a word...
      </p>
    </div>
  );
};

export default GuesserWaitForWordAnimation;
