import { getUserIdFromToken } from "@/lib/jwt";
import { useLocation, useNavigate } from "react-router";
import WebsiteStamp from "../WebsiteStamp";
import { sectionContents, sections } from "./staticData";
import Section from "./Section";
import SectionContent from "./SectionContent";
import { useEffect, useState } from "react";
import AccountSection from "./AccountSection";

const Navbar = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const [currentSectionItem, setCurrentSectionItem] =
    useState<string>("Dashboard");

  useEffect(() => {
    setCurrentSectionItem(location.pathname);
  }, [location.pathname]);

  return (
    <div className="w-90 h-full flex flex-col justify-evenly items-center border-r border-r-neutral-600">
      <div className="w-full h-1/7">
        <WebsiteStamp
          className="text-4xl flex justify-center items-center"
          logoWidth={60}
          logoHeight={60}
        />
        <div className="w-full border-b border-b-neutral-600"></div>
      </div>

      <div className="w-full h-5/7 space-y-8">
        {sections.map(({ id, sectionName }) => (
          <Section className="space-y-2" key={id} sectionName={sectionName}>
            {sectionContents[id].map(({ id: contentId, name, url }) => {
              const token = localStorage.getItem("accessToken"); // stored after login
              const userId = getUserIdFromToken(token || "");

              if (!userId) {
                return (
                  <p
                    key={`error-${contentId}`}
                    className="text-lg font-bold text-red-400"
                  >
                    Unresolved error in the sidebar code
                  </p>
                );
              }

              const resolvedUrl = typeof url === "function" ? url(userId) : url;
              const isActive = currentSectionItem === resolvedUrl;

              return (
                <SectionContent
                  className={`${isActive ? "bg-indigo-500" : ""}`}
                  key={contentId}
                  name={name}
                  handleOnClick={() => {
                    navigate(resolvedUrl);
                  }}
                />
              );
            })}
          </Section>
        ))}
      </div>
      <div className="px-3 w-full h-1/7 flex justify-center items-center">
        <AccountSection />
      </div>
    </div>
  );
};

export default Navbar;
