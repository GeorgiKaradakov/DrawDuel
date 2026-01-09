import type { ServerMessage } from "./types";

let socket: WebSocket | null = null;
const listeners: ((data: ServerMessage) => void)[] = [];

export function connectSocket(
  onOpen?: () => void,
  onClose?: () => void,
): WebSocket {
  if (socket && socket.readyState === WebSocket.OPEN) {
    return socket;
  }

  if (socket && socket.readyState === WebSocket.CONNECTING) {
    return socket;
  }

  const token = localStorage.getItem("accessToken");
  socket = new WebSocket(`ws://localhost:8080/ws/draw?token=${token}`);

  socket.onopen = () => {
    onOpen?.();
  };

  socket.onmessage = (event) => {
    try {
      const data = JSON.parse(event.data);
      listeners.forEach((listener) => listener(data));
    } catch (e) {
      console.error("Bad WS message:", event.data);
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
    console.warn("WebSocket not ready — message dropped:", data);
  }
}

export function addSocketListener(fn: (data: ServerMessage) => void) {
  if (!listeners.includes(fn)) listeners.push(fn);
}

export function removeSocketListener(fn: (data: ServerMessage) => void) {
  const idx = listeners.indexOf(fn);
  if (idx !== -1) listeners.splice(idx, 1);
}

export function disconnectSocket() {
  if (socket) {
    socket.close();
    socket = null;
  }
}

export function getSocket(): WebSocket | null {
  return socket;
}

export function isSocketConnected(): boolean {
  return socket?.readyState === WebSocket.OPEN;
}
