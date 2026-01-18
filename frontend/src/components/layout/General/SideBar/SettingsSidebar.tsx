import { useEffect, useState } from "react";
import SectionContent from "./SectionContent";
import { profileLinks, profileSections } from "./staticData";
import Section from "./Section";
import { cn } from "@/lib/utils";

const SECTION_IDS = [
  "account-settings",
  "security-settings",
  "device-management",
];

const SettingsSidebar = () => {
  const [currentLink, setCurrentLink] = useState<string>("account-settings");

  const scrollToSection = (id: string) => {
    const el = document.getElementById(id);
    if (!el) return;

    el.scrollIntoView({
      behavior: "smooth",
      block: "start",
    });
  };

  useEffect(() => {
    const container = document.getElementById("settings-scroll-container");
    if (!container) return;

    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            setCurrentLink(entry.target.id);
          }
        });
      },
      {
        root: container,
        rootMargin: "-40% 0px -40% 0px",
        threshold: 0.1,
      },
    );

    SECTION_IDS.forEach((id) => {
      const el = document.getElementById(id);
      if (el) observer.observe(el);
    });

    return () => observer.disconnect();
  }, []);

  const isActive = (id: string) => currentLink === id;

  return (
    <aside className="w-100 h-full flex flex-col items-center border-r border-neutral-600">
      {/* Header */}
      <div className="w-full h-20 flex justify-center items-center">
        <p className="text-4xl text-neutral-50 font-bold">Settings</p>
      </div>

      {/* Navigation */}
      <div className="w-full flex-1 space-y-6 px-2">
        {profileSections.map(({ id: sectionId, name }) => (
          <Section key={sectionId} sectionName={name} className="space-y-2">
            {profileLinks[sectionId].map(({ id, name, Icon }) => (
              <SectionContent
                key={id}
                name={name}
                icon={<Icon className="text-neutral-50" />}
                className={cn(
                  "ml-10 space-x-7 transition-all duration-200",
                  isActive(id)
                    ? "bg-neutral-600 border-l-4 border-indigo-500"
                    : "hover:bg-neutral-700",
                )}
                handleOnClick={() => {
                  setCurrentLink(id);
                  scrollToSection(id);
                }}
              />
            ))}
          </Section>
        ))}
      </div>
    </aside>
  );
};

export default SettingsSidebar;
