import { z } from "zod";

export const registerSchema = z
  .object({
    username: z.string().min(4, "Username must be atleast 4 characters!"),
    email: z.string().email("Please enter a valid email!"),
    pass: z
      .string()
      .min(6, "Password must be atleast 6 characters!")
      .regex(/"[A-Z]"/, "Password must contain at least one uppercase letter!")
      .regex(/"[0-9]"/, "Password must contain at least one digit!")
      .regex(
        /"[^A-Za-z0-9]"/,
        "Password must contain at least one special symbol!",
      ),
    passRepeat: z.string(),
    terms: z.literal(true, {
      message: "You must accept the terms and conditions",
    }),
  })
  .refine((data) => data.pass === data.passRepeat, {
    path: ["ConfirmPassword"],
    message: "Passwords do not match!",
  });
