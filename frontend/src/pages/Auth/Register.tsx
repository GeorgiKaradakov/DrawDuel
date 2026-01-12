import { zodResolver } from "@hookform/resolvers/zod";
import { registerSchema } from "@/lib/zodSchemas";
import { useForm } from "react-hook-form";
import z from "zod";
import { Form } from "@/components/ui/form";
import FormInput from "@/components/layout/Auth/FormInput";
import { Button } from "@/components/ui/button";
import { useNavigate } from "react-router";
import { register } from "./auth";

const Register = () => {
  const navigate = useNavigate();
  const form = useForm<z.infer<typeof registerSchema>>({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      username: "",
      email: "",
      pass: "",
      passRepeat: "",
    },
  });

  const onSubmit = async (values: z.infer<typeof registerSchema>) => {
    try {
      await register(
        values.username,
        values.email,
        values.pass,
        values.passRepeat,
      );

      // ✅ user is immediately authenticated (token + cookie)
      navigate("/dashboard");
    } catch (err: any) {
      const message: string = err.message;

      if (message.includes("Username")) {
        form.setError("username", { message });
      } else if (message.includes("Email")) {
        form.setError("email", { message });
      } else {
        form.setError("root", { message });
      }
    }
  };

  return (
    <div className="py-8 pb-14 flex flex-col justify-center items-center space-y-12">
      <p className="pt-5 text-neutral-200 font-semibold text-4xl">
        Register to DrawDuel
      </p>
      <Form {...form}>
        <form
          className="w-full h-full flex flex-col justify-center items-center space-y-8"
          onSubmit={form.handleSubmit(onSubmit)}
        >
          <div className="w-full flex flex-col justify-center items-center space-y-2">
            <FormInput
              formControl={form.control}
              name="username"
              label="Username:"
              placeholder="username ..."
              className="w-4/5 text-lg"
            />

            <FormInput
              formControl={form.control}
              name="email"
              label="Email:"
              type="email"
              placeholder="example@email.com"
              className="w-4/5 text-lg"
            />

            <FormInput
              formControl={form.control}
              name="pass"
              label="Password:"
              type="password"
              putPasVisibilityToggle={true}
              placeholder="password ..."
              className="w-4/5 text-md"
              description="The password must be at least 6 characters long and it must contain at least one uppercase letter, one digit and one special symbol!"
            />

            <FormInput
              formControl={form.control}
              name="passRepeat"
              label="Confirm Password:"
              type="password"
              putPasVisibilityToggle={true}
              placeholder="confirm password:"
              className="w-4/5 text-md"
              description="The confirmed password must be at least 6 characters long and it must contain at least one uppercase letter, one digit and one special symbol and it needs to match your chosen password!"
            />

            <FormInput
              formControl={form.control}
              name="terms"
              label="Agree to Terms and Conditions"
              type="checkbox"
              className="w-4/5"
              data-cy="agreeTerms"
            />
          </div>

          <div className="pt-6 w-full flex flex-col justify-center items-center space-y-3">
            <Button
              variant="default"
              className="bg-violet-500 w-4/5 text-2xl text-white font-bold hover:bg-violet-600 hover:cursor-pointer"
              size="lg"
              type="submit"
              data-cy="register-submit"
            >
              Register
            </Button>
            <div className="w-full flex flex-col justify-center items-center space-y-1">
              <p className="text-neutral-200 text-lg font-semibold space-y-1">
                Already have an account?
              </p>
              <Button
                variant="secondary"
                className="w-4/5 text-2xl text-neutral-200 font-semibold bg-neutral-600 hover:bg-neutral-700 hover:cursor-pointer"
                size="lg"
                type="button"
                onClick={() => navigate("/auth/login")}
              >
                Sign In
              </Button>
            </div>
          </div>
        </form>
      </Form>
    </div>
  );
};

export default Register;
