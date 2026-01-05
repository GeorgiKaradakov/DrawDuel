import { jwtDecode } from "jwt-decode";

interface DecodedToken {
  sub: string; // userId
  username: string;
  email: string;
  exp: number;
}

export function getUserIdFromToken(token: string): string | null {
  try {
    const decoded = jwtDecode<DecodedToken>(token);
    return decoded.sub;
  } catch (error) {
    console.error("Invalid token:", error);
    return null;
  }
}
