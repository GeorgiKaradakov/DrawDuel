import FormInput from "@/components/layout/Auth/FormInput";
import { Button } from "@/components/ui/button";
import { Form } from "@/components/ui/form";
import { securitySettingsSchema } from "@/lib/zodSchemas";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import type z from "zod";
import { updatePassword } from "./server";
import { useState } from "react";
import { Spinner } from "@/components/layout/General/Spinner";
import { errorToast, successToast } from "@/lib/toast";

const SecuritySettings = () => {
  const [loading, setLoading] = useState(false);

  const form = useForm<z.infer<typeof securitySettingsSchema>>({
    resolver: zodResolver(securitySettingsSchema),
    defaultValues: {
      currentPass: "",
      newPass: "",
    },
  });

  const onSubmit = async (values: z.infer<typeof securitySettingsSchema>) => {
    try {
      setLoading(true);
      await updatePassword(values.currentPass, values.newPass).then(() => {
        successToast("Password updated successfully!");
      });
      form.reset();
    } catch (error: any) {
      const errorMsg: string = error.response?.data;
      if (errorMsg?.includes("password")) {
        form.setError("currentPass", { message: errorMsg });
      } else {
        errorToast("Failed to update password.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <Form {...form}>
      <div className="w-7/8 h-fit space-y-4 border-1 border-neutral-600 rounded-md p-6 py-8 gap-y-8">
        <div className="space-y-2">
          <p className="text-3xl text-neutral-50 font-bold">Scurity Settings</p>
          <p className="text-lg text-neutral-400 font-semibold">
            Modify your current password!
          </p>
        </div>
        <form
          className="flex flex-col justify-center items-end gap-y-5"
          onSubmit={form.handleSubmit(onSubmit)}
        >
          <div className="w-full flex justify-center items-center gap-x-5">
            <FormInput
              formControl={form.control}
              name="currentPass"
              label="Current Password:"
              type="password"
              putPasVisibilityToggle={true}
              placeholder="current password ..."
              data_cy="current-password-input"
              description="The password must be at least 6 characters long and it must contain at least one uppercase letter, one digit and one special symbol!"
            />

            <FormInput
              formControl={form.control}
              name="newPass"
              label="New Password:"
              type="password"
              putPasVisibilityToggle={true}
              placeholder="new password ..."
              data_cy="new-password-input"
              description="The password must be at least 6 characters long and it must contain at least one uppercase letter, one digit and one special symbol!"
            />
          </div>

          <Button
            variant="default"
            type="submit"
            disabled={loading}
            data-cy="change-password-button"
            className="p-4 text-md font-bold bg-indigo-500 hover:bg-indigo-400 min-w-[180px]"
          >
            {loading ? <Spinner /> : "Change Password"}
          </Button>
        </form>
      </div>
    </Form>
  );
};

export default SecuritySettings;
