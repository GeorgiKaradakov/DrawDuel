import { useState } from "react";
import SectionContent from "./SectionContent";
import { profileLinks, profileSections } from "./staticData";
import Section from "./Section";

const SettingsSidebar = () => {
  const [currentLink, setCurrentLink] = useState<string>("account-settings");
  const activeLink = (link: string) => currentLink === link;
  return (
    <div className="w-100 h-full flex flex-col justify-evenly items-center border-r-1 border-y-1 border-neutral-600 space-y-12">
      <div className="w-full h-20 flex justify-center items-center">
        <p className="text-4xl text-neutral-50 font-bold">Settings</p>
      </div>
      <div className="w-full h-full space-y-3">
        {profileSections.map(({ id, name }) => (
          <Section key={id} className="space-y-2" sectionName={name}>
            {profileLinks[id].map(({ id, name, Icon }) => (
              <SectionContent
                key={id}
                className={`ml-10 ${activeLink(id) ? "bg-neutral-500" : ""} space-x-7`}
                name={name}
                icon={<Icon className="text-neutral-50" />}
                handleOnClick={() => {
                  setCurrentLink(id);
                }}
              />
            ))}
          </Section>
        ))}
      </div>
    </div>
  );
};

export default SettingsSidebar;
