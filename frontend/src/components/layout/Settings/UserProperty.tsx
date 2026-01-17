import { cn } from "@/lib/utils";
import type { UserPropertyProps } from "@/pages/UserSettings/types";
import { Button } from "@/components/ui/button";
import { useRef, useState } from "react";
import { api, apiWithImage } from "@/lib/axios";

const UserProperty = ({
  className,
  title,
  content,
  isImage,
}: UserPropertyProps) => {
  const [updatedContent, setUpdatedContent] = useState(content);
  const [loading, setLoading] = useState(false);

  const fileInputRef = useRef<HTMLInputElement | null>(null);

  const handleTextSubmit = async () => {
    if (updatedContent === content || !updatedContent) return;

    try {
      setLoading(true);

      const endpoint =
        title === "Username"
          ? "/api/user/update-username"
          : "/api/user/update-email";

      await api.patch(endpoint, {
        value: updatedContent,
      });
    } finally {
      setLoading(false);
    }
  };

  const handleImageChange = async (file: File) => {
    const formData = new FormData();
    formData.append("profileImage", file);

    try {
      setLoading(true);
      await apiWithImage.post("/api/user/update-profile-image", formData);
    } finally {
      setLoading(false);
    }
  };

  const handleImageRemove = async () => {
    try {
      setLoading(true);
      await api.delete("/api/user/profile-image");
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
              onChange={(e) => {
                const file = e.target.files?.[0];
                if (file) handleImageChange(file);
              }}
            />

            <Button
              variant="destructive"
              onClick={handleImageRemove}
              disabled={loading}
            >
              Remove Image
            </Button>

            <Button
              onClick={() => fileInputRef.current?.click()}
              disabled={loading}
              className="bg-indigo-500 hover:bg-indigo-400"
            >
              Change Image
            </Button>
          </>
        )}

        {!isImage && (
          <Button
            onClick={handleTextSubmit}
            disabled={loading || updatedContent === content}
            className="bg-indigo-500 hover:bg-indigo-400"
          >
            Save Changes
          </Button>
        )}
      </div>
    </div>
  );
};

export default UserProperty;
