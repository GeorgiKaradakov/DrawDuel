import { Outlet } from "react-router";

const AuthLayout = () => {
  return (
    <div className="relative w-screen h-screen overflow-hidden">
      <img
        src="/auth_background.png"
        className="absolute inset-0 w-full h-full object-fit z-0"
        alt="auth bg"
      />
      <div className="absolute inset-0 bg-black/45 backdrop-blur-xs z-5"></div>
      <div className="absolute inset-0 w-full h-full z-10">
        <div className="absolute left-4/7 top-1/2 w-3/9 h-2/3 bg-neutral-900 border border-neutral-700 rounded-2xl -translate-y-1/2">
          <Outlet />
        </div>
      </div>
    </div>
  );
};

export default AuthLayout;
