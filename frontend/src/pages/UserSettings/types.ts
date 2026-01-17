import z from "zod";

export interface UserPropertyProps {
  className?: string;
  title?: string;
  content: string;
  isImage: boolean;
}
