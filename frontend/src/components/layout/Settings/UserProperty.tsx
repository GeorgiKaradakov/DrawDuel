import { cn } from "@/lib/utils";
import type { UserPropertyProps } from "@/pages/UserSettings/types";
import { Button } from "@/components/ui/button";
import { useRef, useState } from "react";
import { api } from "@/lib/axios";
import {
  updateEmail,
  updateProfileImage,
  updateUsername,
} from "@/pages/UserSettings/server";
import { Spinner } from "../General/Spinner";
import { errorToast, successToast } from "@/lib/toast";
import { useAuth } from "@/context/authProvider/useAuth";

const UserProperty = ({
  className,
  title,
  content,
  isImage,
}: UserPropertyProps) => {
  const { updateUser } = useAuth();
  const [updatedContent, setUpdatedContent] = useState(content);
  const [loading, setLoading] = useState(false);

  const fileInputRef = useRef<HTMLInputElement | null>(null);

  const handleTextSubmit = async () => {
    if (updatedContent === content || !updatedContent) return;

    try {
      setLoading(true);

      switch (title) {
        case "Username":
          await updateUsername(updatedContent)
            .then(() => {
              successToast("Username updated successfully!");
              updateUser({ username: updatedContent });
            })
            .catch((error) => {
              errorToast(error.response?.data || "Failed to update username.");
            });
          break;

        case "Email":
          await updateEmail(updatedContent)
            .then(() => {
              successToast("Email updated successfully!");
              updateUser({ email: updatedContent });
            })
            .catch((error) => {
              errorToast(error.response?.data || "Failed to update email.");
            });
          break;

        default:
          break;
      }
    } finally {
      setLoading(false);
    }
  };

  const handleImageChange = async (file: File) => {
    setLoading(true);
    await updateProfileImage(file)
      .then(() => {
        successToast("Profile image updated successfully!");
        updateUser({ profileImageUrl: URL.createObjectURL(file) });
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const handleImageRemove = async () => {
    try {
      setLoading(true);
      await api
        .delete("/api/user/delete-profile-image")
        .then((result) => {
          successToast("Profile image removed successfully!");
          updateUser({ profileImageUrl: result?.data });
        })
        .catch((error) => {
          errorToast(error.response?.data || "Failed to remove profile image.");
        });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      className={cn(
        "w-full h-20 flex justify-between items-center gap-6",
        className,
      )}
    >
      {title && (
        <p className="w-40 text-lg text-neutral-50 font-bold">{title}</p>
      )}

      {isImage ? (
        <img
          className="rounded-full object-cover border border-neutral-700"
          src={content || "/avatar-placeholder.png"}
          width={120}
          height={120}
          alt={title}
        />
      ) : (
        <input
          className="px-5 text-lg text-neutral-50 font-semibold border border-neutral-600 bg-neutral-800 p-2 rounded-md w-2/5 outline-none"
          value={updatedContent}
          data-cy={"change-" + title + "-input"}
          onChange={(e) => setUpdatedContent(e.target.value)}
        />
      )}

      <div className="flex justify-center items-center space-x-3">
        {isImage && (
          <>
            <input
              ref={fileInputRef}
              type="file"
              hidden
              accept="image/*"
              data-cy="change-profile-image-input"
              onChange={(e) => {
                const file = e.target.files?.[0];
                if (file) handleImageChange(file);
              }}
            />

            <Button
              variant="destructive"
              onClick={handleImageRemove}
              disabled={loading}
              className="min-w-[140px]"
              data-cy="remove-profile-image-button"
            >
              {loading ? <Spinner /> : "Remove Image"}
            </Button>

            <Button
              onClick={() => fileInputRef.current?.click()}
              disabled={loading}
              className="bg-indigo-500 hover:bg-indigo-400 min-w-[140px]"
              data-cy="change-profile-image-button"
            >
              {loading ? <Spinner /> : "Change Image"}
            </Button>
          </>
        )}

        {!isImage && (
          <Button
            onClick={handleTextSubmit}
            disabled={loading || updatedContent === content}
            data-cy={"change-" + title + "-submit"}
            className="bg-indigo-500 hover:bg-indigo-400 min-w-[140px]"
          >
            {loading ? <Spinner /> : "Save Changes"}
          </Button>
        )}
      </div>
    </div>
  );
};

export default UserProperty;
