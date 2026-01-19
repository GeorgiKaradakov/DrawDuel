import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { cn } from "@/lib/utils";
import type { GenericTableProps } from "../types";

export function GenericTable<T>({
  data,
  columns,
  className,
  tableHeaderClassName,
}: GenericTableProps<T>) {
  return (
    <div
      className={cn(
        "max-h-54 overflow-y-auto scrollbar-thin scrollbar-thumb-neutral-500 scrollbar-track-neutral-700 rounded-md",
        className,
      )}
    >
      <Table className="w-full border-collapse">
        <TableHeader className="sticky top-0 bg-neutral-700 z-10">
          <TableRow
            className={cn("border-b border-neutral-500", tableHeaderClassName)}
          >
            {columns.map((col) => (
              <TableHead
                key={String(col.key)}
                className="text-neutral-300 font-bold"
              >
                {col.label}
              </TableHead>
            ))}
          </TableRow>
        </TableHeader>
        <TableBody>
          {data.map((row, rowIndex) => (
            <TableRow
              key={rowIndex}
              className="border-b border-neutral-600 hover:bg-neutral-700/60 transition"
            >
              {columns.map((col) => (
                <TableCell key={String(col.key)} className={col.className}>
                  {col.render
                    ? col.render(row[col.key], row)
                    : String(row[col.key])}
                </TableCell>
              ))}
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
}
