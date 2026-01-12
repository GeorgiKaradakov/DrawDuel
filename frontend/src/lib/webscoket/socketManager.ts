import { getAccessToken } from "../api";

let socket: WebSocket | null = null;
const listeners: ((data: any) => void)[] = [];

export function connectSocket(onOpen?: () => void, onClose?: () => void) {
  if (socket && socket.readyState === WebSocket.OPEN) {
    return socket;
  }

  const token = getAccessToken();
  socket = new WebSocket(`ws://localhost:8080/ws/drawduel?token=${token}`);

  socket.onopen = () => {
    console.log("✅ WS connected");
    onOpen?.();
  };

  socket.onclose = () => {
    console.log("❌ WS disconnected");
    onClose?.();
    socket = null;
  };

  socket.onmessage = (event) => {
    try {
      const data = JSON.parse(event.data);
      listeners.forEach((l) => l(data));
    } catch (e) {
      console.error("Bad WS message:", event.data);
    }
  };

  return socket;
}

export function sendMessage(payload: any) {
  if (!socket || socket.readyState !== WebSocket.OPEN) {
    console.warn("WebSocket not ready — message dropped:", payload);
    return;
  }
  socket.send(JSON.stringify(payload));
}

export function addSocketListener(fn: (data: any) => void) {
  listeners.push(fn);
}

export function removeSocketListener(fn: (data: any) => void) {
  const idx = listeners.indexOf(fn);
  if (idx >= 0) listeners.splice(idx, 1);
}

export function disconnectSocket() {
  socket?.close();
  socket = null;
}
