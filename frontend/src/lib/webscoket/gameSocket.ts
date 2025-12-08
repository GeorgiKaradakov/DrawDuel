import { sendMessage } from "./socketManager";
import type { DrawEvent } from "./types";

export function searchForGame() {
  sendMessage({ type: "searching" });
}

export function sendDrawEvent(event: DrawEvent) {
  sendMessage(event);
}
