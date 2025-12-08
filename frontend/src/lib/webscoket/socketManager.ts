import type { ServerMessage } from "./types";

let socket: WebSocket | null = null;

export function connectSocket(
  onMessage: (msg: ServerMessage) => void,
  onOpen?: () => void,
  onClose?: () => void,
) {
  if (socket && socket.readyState === WebSocket.OPEN) return socket;

  socket = new WebSocket("ws://localhost:8080/ws/draw");

  socket.onopen = () => {
    console.log("Connected to WebSocket");
    onOpen?.();
  };

  socket.onmessage = (event) => {
    try {
      const data = JSON.parse(event.data);
      onMessage(data);
    } catch (e) {
      console.error("Invalid message:", event.data);
    }
  };

  socket.onclose = () => {
    console.log("Socket closed");
    onClose?.();
    socket = null;
  };

  socket.onerror = (err) => {
    console.error("WebSocket error:", err);
  };

  return socket;
}

export function sendMessage(data: any) {
  if (socket?.readyState === WebSocket.OPEN) {
    socket.send(JSON.stringify(data));
  } else {
    console.warn("WebSocket not ready, message dropped:", data);
  }
}

export function disconnectSocket() {
  if (socket) {
    socket.close();
    socket = null;
  }
}

export function getSocket() {
  return socket;
}
