import type { FieldValues } from "react-hook-form";
import type { FormInputProps } from "./types";
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { useState } from "react";
import { Checkbox } from "@/components/ui/checkbox";
import { cn } from "@/lib/utils";
import { Eye, EyeOff } from "lucide-react";
import FormDescrriptionToolTip from "./FormDescrriptionToolTip";
import { Button } from "@/components/ui/button";

function FormInput<T extends FieldValues>({
  formControl,
  name,
  label,
  type = "text",
  placeholder,
  valid = true,
  description,
  putPasVisibilityToggle,
  className,
  inputClassName,
}: FormInputProps<T>) {
  const [showPass, setShowPass] = useState(false);
  const inputType =
    type === "password" && putPasVisibilityToggle && showPass ? "text" : type;
  return (
    <FormField
      control={formControl}
      name={name}
      render={({ field }) => (
        <FormItem className={cn("text-white w-full h-full", className)}>
          {inputType === "text" ||
          inputType === "email" ||
          inputType === "password" ? (
            <>
              <FormLabel className="-ml-1 text-md">{label}</FormLabel>
              {putPasVisibilityToggle ? (
                <div className="relative w-full h-full rounded-sm border border-neutral-700">
                  <FormDescrriptionToolTip description={description ?? ""}>
                    <>
                      <input
                        type={inputType}
                        placeholder={placeholder}
                        disabled={!valid}
                        className={cn(
                          "w-full h-full p-2 pl-5 rounded-sm bg-neutral-800 focus:outline-none",
                          inputClassName,
                        )}
                        {...field}
                      />
                      <Button
                        type="button"
                        variant={"default"}
                        size={"icon"}
                        className="absolute -right-1 top-1/2 -translate-1/2 h-full bg-transparent hover:cursor-pointer hover:scale-110 hover:bg-transparent select-none"
                        onClick={() => setShowPass((p) => !p)}
                      >
                        {showPass ? (
                          <EyeOff className="size-7" />
                        ) : (
                          <Eye className="size-7" />
                        )}
                      </Button>
                    </>
                  </FormDescrriptionToolTip>
                </div>
              ) : (
                <FormDescrriptionToolTip description={description ?? ""}>
                  <FormControl className="border border-neutral-700">
                    <input
                      type={inputType}
                      placeholder={placeholder}
                      disabled={!valid}
                      className={cn(
                        "w-full h-full p-2 pl-5 rounded-sm bg-neutral-800 focus:outline-none",
                        inputClassName,
                      )}
                      {...field}
                    />
                  </FormControl>
                </FormDescrriptionToolTip>
              )}
            </>
          ) : inputType === "checkbox" ? (
            <>
              <div className="flex justify-start items-center space-x-2">
                <FormControl>
                  <Checkbox
                    // data-cy={props["data-cy"]}
                    checked={field.value}
                    onCheckedChange={field.onChange}
                    className={cn(inputClassName, "")}
                    disabled={!valid}
                  />
                </FormControl>
                <FormLabel className="text-md">{label}</FormLabel>
              </div>
            </>
          ) : (
            <></>
          )}
          <FormMessage />
        </FormItem>
      )}
    />
  );
}

export default FormInput;
