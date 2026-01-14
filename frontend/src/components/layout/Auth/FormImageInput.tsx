import { useDropzone } from "react-dropzone";
import type { FieldValues } from "react-hook-form";
import type { FormInputProps } from "./types";
import { useCallback, useEffect, useState } from "react";
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { cn } from "@/lib/utils";

const FormImageInput = <T extends FieldValues>({
  formControl,
  name,
  label,
  className,
}: FormInputProps<T>) => {
  const [preview, setPreview] = useState<string | null>(null);

  const onDrop = useCallback(
    (acceptedFiles: File[], onChange: (file: File | null) => void) => {
      const file = acceptedFiles[0];
      if (!file) return;

      onChange(file);
      setPreview(URL.createObjectURL(file));
    },
    [],
  );

  // Cleanup preview URL
  useEffect(() => {
    return () => {
      if (preview) URL.revokeObjectURL(preview);
    };
  }, [preview]);

  return (
    <FormField
      control={formControl}
      name={name}
      render={({ field }) => {
        const { getRootProps, getInputProps, isDragActive } = useDropzone({
          accept: { "image/*": [] },
          multiple: false,
          onDrop: (files) => onDrop(files, field.onChange),
        });

        return (
          <FormItem className={cn("w-full", className)}>
            <FormLabel className="text-lg text-neutral-50">{label}</FormLabel>

            <FormControl>
              <div
                {...getRootProps()}
                className={cn(
                  "flex items-center justify-center",
                  "h-40 w-full rounded-md border-2 border-dashed",
                  "bg-neutral-800 text-neutral-400 cursor-pointer transition",
                  isDragActive
                    ? "border-primary bg-neutral-700"
                    : "border-neutral-600 hover:border-neutral-400",
                )}
              >
                <input {...getInputProps()} />

                {preview ? (
                  <img
                    src={preview}
                    alt="Image preview"
                    className="h-28 w-28 rounded-full object-cover"
                  />
                ) : (
                  <p className="text-sm text-center px-4">
                    Drag & drop an image here, or click to select
                  </p>
                )}
              </div>
            </FormControl>

            <FormMessage />
          </FormItem>
        );
      }}
    />
  );
};

export default FormImageInput;
