import { sendMessage } from "./socketManager";
import type { DrawEvent } from "./types";

export function searchForGame(userId: string) {
  sendMessage({ type: "search", userId });
}

export function chooseWord(word: string) {
  sendMessage({ type: "chooseWord", word });
}

export function chooseWordTimeOut(words: string[]) {
  sendMessage({ type: "timeUpWordChoice", words });
}

export function sendDrawEvent(event: DrawEvent) {
  sendMessage({ type: "draw", ...event });
}

export function sendGuess(guess: string, timeLeft: number, guessCount: number) {
  sendMessage({ type: "guess", guess, timeLeft, guessCount });
}
