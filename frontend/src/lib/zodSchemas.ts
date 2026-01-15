import { z } from "zod";

const MAX_IMAGE_SIZE = 2 * 1024 * 1024; // 2MB
const ACCEPTED_IMAGE_TYPES = ["image/jpeg", "image/png", "image/webp"];

export const registerSchema = z
  .object({
    username: z.string().min(4, "Username must be atleast 4 characters!"),

    email: z.string().email("Please enter a valid email address!"),

    pass: z
      .string()
      .min(6, "Password must be atleast 6 characters!")
      .regex(/[A-Z]/, "Password must contain at least one uppercase letter!")
      .regex(/[0-9]/, "Password must contain at least one digit!")
      .regex(
        /[^A-Za-z0-9]/,
        "Password must contain at least one special symbol!",
      ),

    passRepeat: z
      .string()
      .min(6, "Confirmed password must be atleast 6 characters!")
      .regex(
        /[A-Z]/,
        "Confirmed password must contain at least one uppercase letter!",
      )
      .regex(/[0-9]/, "Confirmed password must contain at least one digit!")
      .regex(
        /[^A-Za-z0-9]/,
        "Confirmed password must contain at least one special symbol!",
      ),

    profileImage: z
      .instanceof(File)
      .optional()
      .refine(
        (file) => !file || file.size <= MAX_IMAGE_SIZE,
        "Image must be smaller than 2MB",
      )
      .refine(
        (file) => !file || ACCEPTED_IMAGE_TYPES.includes(file.type),
        "Only JPG, PNG, or WEBP images are allowed",
      ),

    terms: z.literal(true, {
      message: "You must accept the terms and conditions",
    }),
  })
  .superRefine((data, ctx) => {
    if (data.pass !== data.passRepeat) {
      ctx.addIssue({
        code: "custom",
        message: "Passwords do not match!",
        path: ["passRepeat"],
      });
    }
  });

export const loginSchema = z.object({
  identifier: z
    .string()
    .min(4, "Please enter a valid username or email!")
    .refine((val) => {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return emailRegex.test(val) || val.length >= 4;
    }, "Please enter a valid username or email!"),
  pass: z
    .string()
    .min(6, "Confirmed password must be atleast 6 characters!")
    .regex(
      /[A-Z]/,
      "Confirmed password must contain at least one uppercase letter!",
    )
    .regex(/[0-9]/, "Confirmed password must contain at least one digit!")
    .regex(
      /[^A-Za-z0-9]/,
      "Confirmed password must contain at least one special symbol!",
    ),
});
