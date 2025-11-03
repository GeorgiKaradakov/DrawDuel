import type { Control, FieldValues, Path } from "react-hook-form";

export type FormInputProps<T extends FieldValues> = {
  formControl: Control<T>;
  name: Path<T>;
  label: string;
  type?: string;
  placeholder?: string;
  valid?: boolean;
  description?: string;
  putPasVisibilityToggle?: boolean;
  className?: string;
  inputClassName?: string;
};
