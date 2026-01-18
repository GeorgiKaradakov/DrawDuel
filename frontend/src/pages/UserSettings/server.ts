import { api, apiWithImage } from "@/lib/axios";

export const updateUsername = async (username: string) => {
  await api
    .patch("/api/user/update-username", { value: username })
    .then((response) => {
      console.log(response);
      return response.data;
    })
    .catch((error) => {
      console.log(error);
    });
};

export const updateEmail = async (email: string) => {
  await api
    .patch("/api/user/update-email", { value: email })
    .then((response) => {
      console.log(response);
      return response.data;
    })
    .catch((error) => {
      console.log(error);
    });
};

export const updateProfileImage = async (file: File) => {
  const formData = new FormData();
  formData.append("profileImage", file);

  await apiWithImage
    .post("/api/user/update-profile-image", formData)
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      console.log(error);
    });
};

export const updatePassword = async (currentPass: string, newPass: string) => {
  return await api.post("/api/user/change-password", {
    currentPassword: currentPass,
    newPassword: newPass,
  });
};

export const getDevicesData = async () => {
  return await api.get("/api/user/get-devices");
};

export const revokeDevice = async (deviceId: string) => {
  return await api.delete(`/api/user/revoke-session/${deviceId}`);
};

export const deleteAccount = async () => {
  await api.delete("/api/user/delete-account").catch((error) => {
    console.log(error);
  });
};
