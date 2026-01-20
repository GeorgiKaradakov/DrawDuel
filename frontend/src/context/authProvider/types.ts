export type AuthUser = {
  id: string;
  username: string;
  email: string;
  profileImageUrl?: string | null;
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
  updateUser: (updatedUser: Partial<AuthUser>) => void;
  initAuth: () => Promise<void>;
};

export type AuthProviderProps = {
  children: React.ReactNode;
};
