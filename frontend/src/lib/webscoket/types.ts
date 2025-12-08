export interface DrawEvent {
  tool: { name: string; strokeWidth: number };
  color: string;
  points: number[];
}

export type ServerMessage =
  | { type: "waiting"; message: string }
  | { type: "start"; message: string }
  | { type: "disconnect"; message: string }
  | { type: "error"; message: string }
  | DrawEvent;
