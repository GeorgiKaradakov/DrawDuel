import FormInput from "@/components/layout/Auth/FormInput";
import { Form } from "@/components/ui/form";
import { securitySettingsSchema } from "@/lib/zodSchemas";
import { useForm } from "react-hook-form";

const SecuritySettings = () => {
  const form = useForm<z.infer<typeof securitySettingsSchema>>({
    defaultValues: {
      currentPassword: "",
      newPassword: "",
    },
  });

  const onSubmit = async (values: z.infer<typeof securitySettingsSchema>) => {
    // Handle form submission
    console.log(values);
  };

  return (
    <Form {...form}>
      <div className="w-7/8 h-fit space-y-4">
        <div className="space-y-2">
          <p className="text-3xl text-neutral-50 font-bold">Scurity Settings</p>
          <p className="text-lg text-neutral-400 font-semibold">
            Modify your current password!
          </p>
        </div>
        <form className="flex justify-center items-center gap-x-5">
          <FormInput
            formControl={form.control}
            name="currentPassword"
            label="Current Password:"
            type="password"
            putPasVisibilityToggle={true}
            placeholder="current password ..."
            description="The password must be at least 6 characters long and it must contain at least one uppercase letter, one digit and one special symbol!"
          />

          <FormInput
            formControl={form.control}
            name="newPassword"
            label="New Password:"
            type="password"
            putPasVisibilityToggle={true}
            placeholder="new password ..."
            description="The password must be at least 6 characters long and it must contain at least one uppercase letter, one digit and one special symbol!"
          />
        </form>
      </div>
    </Form>
  );
};

export default SecuritySettings;
