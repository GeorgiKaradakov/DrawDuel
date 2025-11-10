export const setAccessToken = (token: string) => {
  localStorage.clear();
  localStorage.setItem("accessToken", token);
};

export const getAccessToken = () => {
  return localStorage.getItem("accessToken");
};
