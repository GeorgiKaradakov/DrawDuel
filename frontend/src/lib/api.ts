let accessToken: string | null = null;

export const setAccessToken = (token: string) => {
  accessToken = token;
  console.log("🔹 Token updated in memory:", token);
};

export const getAccessToken = () => {
  return accessToken;
  console.log("📦 Current token in memory:", accessToken);
};
