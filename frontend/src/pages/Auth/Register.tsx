import { zodResolver } from "@hookform/resolvers/zod";
import { registerSchema } from "@/lib/zodSchemas";
import { useForm } from "react-hook-form";
import z from "zod";
import { Form } from "@/components/ui/form";
import FormInput from "@/components/layout/Auth/FormInput";

const Register = () => {
  const form = useForm<z.infer<typeof registerSchema>>({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      username: "",
      email: "",
      pass: "",
      passRepeat: "",
    },
  });

  const onSubmit = (values: z.infer<typeof registerSchema>) => {
    console.log(values);
  };

  return (
    <div className="flex flex-col justify-center items-center space-y-24">
      <p className="pt-5 text-neutral-200 font-semibold text-4xl">
        Register to DrawDuel
      </p>
      <Form {...form}>
        <form
          className="w-full h-full flex flex-col justify-center items-center space-y-2"
          onSubmit={form.handleSubmit(onSubmit)}
        >
          <FormInput
            formControl={form.control}
            name="username"
            label="Username:"
            placeholder="username ..."
            className="w-3/5 text-md"
          />

          <FormInput
            formControl={form.control}
            name="email"
            label="Email:"
            type="email"
            placeholder="example@email.com"
            className="w-3/5 text-md"
          />

          <FormInput
            formControl={form.control}
            name="pass"
            label="Password:"
            type="password"
            putPasVisibilityToggle={true}
            placeholder="example@email.com"
            className="w-3/5 text-md"
          />
        </form>
      </Form>
    </div>
  );
};

export default Register;
