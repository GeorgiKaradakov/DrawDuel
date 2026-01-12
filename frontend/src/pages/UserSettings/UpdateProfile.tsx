import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { Form } from "@/components/ui/form";
import FormInput from "@/components/layout/Auth/FormInput";
import { Button } from "@/components/ui/button";
import z from "zod";
import { useEffect } from "react";
import { deleteAccountApi, getMyProfile, updateProfile } from "./userSettings";
import { useNavigate } from "react-router";

const updateProfileSchema = z.object({
  username: z.string().min(3),
  email: z.string().email(),
});

type UpdateProfileForm = z.infer<typeof updateProfileSchema>;

const UpdateProfile = () => {
  const navigate = useNavigate();

  const form = useForm<UpdateProfileForm>({
    resolver: zodResolver(updateProfileSchema),
    defaultValues: {
      username: "",
      email: "",
    },
  });

  // 🔥 LOAD PROFILE ON PAGE LOAD
  useEffect(() => {
    const loadProfile = async () => {
      try {
        const profile = await getMyProfile();
        form.reset(profile);
      } catch {
        form.setError("root", {
          message: "Failed to load profile",
        });
      }
    };

    loadProfile();
  }, [form]);

  const onSubmit = async (values: UpdateProfileForm) => {
    try {
      await updateProfile(values.username, values.email);

      form.clearErrors();
      alert("Update successful!");
    } catch (err: any) {
      form.setError("root", {
        message: err?.response?.data ?? "Failed to update profile",
      });
    }
  };

  const deleteAccount = async () => {
    const confirmed = window.confirm(
      "Are you sure you want to delete your account? This action cannot be undone.",
    );

    if (!confirmed) return;

    try {
      await deleteAccountApi();

      navigate("/auth/login");
    } catch (err) {
      alert("Failed to delete account");
    }
  };

  return (
    <div className="w-full h-full flex justify-center items-center">
      <div className="py-8 w-4/7 flex flex-col items-center space-y-12">
        <p className="text-neutral-200 font-semibold text-4xl">
          Update Profile
        </p>

        <Form {...form}>
          <form
            onSubmit={form.handleSubmit(onSubmit)}
            className="w-full flex flex-col items-center space-y-8"
          >
            <FormInput
              formControl={form.control}
              name="username"
              label="Username:"
              className="w-4/5 h-20"
            />

            <FormInput
              formControl={form.control}
              name="email"
              label="Email:"
              type="email"
              className="w-4/5 h-20"
            />

            <div className="w-full flex justify-evenly">
              <Button
                type="submit"
                className="w-1/3 h-10 text-lg font-bold bg-neutral-600"
              >
                Save changes
              </Button>

              <Button
                type="button"
                variant="destructive"
                className="w-1/3 h-10 text-lg font-semibold"
                onClick={deleteAccount}
              >
                Delete account
              </Button>
            </div>

            {form.formState.errors.root && (
              <p className="text-lg text-red-500 font-semibold">
                {form.formState.errors.root.message}
              </p>
            )}
          </form>
        </Form>
      </div>
    </div>
  );
};

export default UpdateProfile;
