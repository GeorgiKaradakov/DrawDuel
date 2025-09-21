import { useState } from "react";
import { Button } from "./components/ui/button";

function App() {
  const [counter, setCounter] = useState(0);
  return (
    <div className="w-screen h-screen flex flex-col justify-center items-center gap-y-4">
      <h1 className="text-5xl text-indigo-400">Hello, summoners! :)</h1>
      <Button
        size={"lg"}
        variant={"destructive"}
        className="text-lg"
        onClick={() => {
          setCounter((x) => x + 1);
        }}
      >
        count: {counter}
      </Button>
    </div>
  );
}

export default App;
