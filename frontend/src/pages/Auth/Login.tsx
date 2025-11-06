import { zodResolver } from "@hookform/resolvers/zod";
import { loginSchema, registerSchema } from "@/lib/zodSchemas";
import { useForm } from "react-hook-form";
import z from "zod";
import { Form } from "@/components/ui/form";
import FormInput from "@/components/layout/Auth/FormInput";
import { Button } from "@/components/ui/button";

const Login = () => {
  const form = useForm<z.infer<typeof loginSchema>>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      identifier: "",
      pass: "",
    },
  });

  const onSubmit = (values: z.infer<typeof loginSchema>) => {
    console.log(values);
    form.reset();
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
              name="identifier"
              label="Username or Email:"
              placeholder="username or email ..."
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
          </div>

          <div className="pt-6 w-full flex flex-col justify-center items-center space-y-3">
            <Button
              variant="default"
              className="bg-violet-500 w-4/5 text-2xl text-white font-bold hover:bg-violet-600 hover:cursor-pointer"
              size="lg"
              type="submit"
            >
              Login
            </Button>
            <div className="w-full flex flex-col justify-center items-center space-y-1">
              <p className="text-neutral-200 text-lg font-semibold space-y-1">
                Don't have an account?
              </p>
              <Button
                variant="secondary"
                className="w-4/5 text-2xl text-neutral-200 font-semibold bg-neutral-600 hover:bg-neutral-700 hover:cursor-pointer"
                size="lg"
                type="button"
              >
                Sign Up
              </Button>
            </div>
          </div>
        </form>
      </Form>
    </div>
  );
};

export default Login;
