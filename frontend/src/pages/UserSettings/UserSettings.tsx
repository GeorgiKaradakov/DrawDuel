import UserProperty from "@/components/layout/Settings/UserProperty";
import { userProperties } from "./static-data";
import { useAuth } from "@/context/authProvider/useAuth";

const UserSettings = () => {
  const { user } = useAuth();

  const resolveContent = (title: string) => {
    switch (title) {
      case "Username":
        return user?.username || "Unknown User";

      case "Email":
        return user?.email || "Unknown User";

      default:
        return "Unknown User";
    }
  };

  return (
    <div className="w-7/8 h-fit space-y-10">
      <p className="text-3xl text-neutral-50 font-bold">Account Settings</p>
      <div className="p-4 py-8 space-y-10 border-1 border-neutral-600 rounded-md">
        {userProperties.map(({ title, content, isImage }) =>
          isImage ? (
            <UserProperty
              key={title}
              content={content(user?.profileImageUrl || "placeholder")}
              isImage={isImage}
            />
          ) : (
            <UserProperty
              key={title}
              title={title}
              content={resolveContent(title)}
              isImage={false}
            />
          ),
        )}
      </div>
    </div>
  );
};

export default UserSettings;
