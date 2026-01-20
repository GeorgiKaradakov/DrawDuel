import type { ReactNode } from "react";
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
  data_cy?: string;
};

export type FormDescriptionToolTipProps = {
  children: ReactNode;
  description: string;
  className?: string;
  textStyle?: string;
};
