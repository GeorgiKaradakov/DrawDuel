export type AuthUser = {
  sub: string;
  exp: number;
};

export type AuthContextType = {
  user: AuthUser | null;
  isAuthenticated: boolean;
  loading: boolean;
  login: (identifier: string, password: string) => Promise<void>;
  register: (
    username: string,
    email: string,
    password: string,
    passsRepeat: string,
    profileImage?: File,
  ) => Promise<void>;
  logout: () => void;
};

export type AuthProviderProps = {
  children: React.ReactNode;
};
