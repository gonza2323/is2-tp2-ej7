import { z } from 'zod';

export const LibroCreateDto = z.object({
  id: z.number().optional(),

  titulo: z
    .string({
      required_error: "El título no puede estar vacío",
    })
    .min(1, "El título no puede estar vacío")
    .max(50, "Máximo 50 caracteres"),

  fecha: z
    .number({
      required_error: "Debe indicar el año de publicación",
      invalid_type_error: "Debe indicar el año de publicación",
    })
    .int()
    .max(9999, "Año inválido"),

  genero: z
    .string({
      required_error: "El género no puede estar vacío",
    })
    .min(1, "El género no puede estar vacío")
    .max(50, "Máximo 50 caracteres"),

  paginas: z
    .number({
      required_error: "Debe indicar la cantidad de páginas",
      invalid_type_error: "Debe indicar la cantidad de páginas",
    })
    .min(1, "La cantidad de páginas debe ser positiva"),

  personaId: z
    .string({ required_error: "Debe indicar la persona dueña del libro" })
    .min(1)
    .transform((val) => Number(val)),

  autoresIds: z
    .array(z.string())
    .min(1, "Debe indicar el/los autor/es")
    .transform((val) => val.map(Number)),

  pdfPath: z.string().optional().nullable(),
});
export type LibroCreateDto = z.infer<typeof LibroCreateDto>;

export const LibroUpdateDto = LibroCreateDto;
export type LibroUpdateDto = z.infer<typeof LibroCreateDto>;

export const LibroSummaryDto = z.object({
  id: z.number().optional(),

  titulo: z
    .string({
      required_error: "El título no puede estar vacío",
    })
    .min(1, "El título no puede estar vacío")
    .max(50, "Máximo 50 caracteres"),

  fecha: z
    .number({
      required_error: "Debe indicar el año de publicación",
      invalid_type_error: "Debe indicar el año de publicación",
    })
    .int()
    .max(9999, "Año inválido"), // optional sanity limit; remove if unnecessary

  genero: z
    .string({
      required_error: "El género no puede estar vacío",
    })
    .min(1, "El género no puede estar vacío")
    .max(50, "Máximo 50 caracteres"),

  paginas: z
    .number({
      required_error: "Debe indicar la cantidad de páginas",
      invalid_type_error: "Debe indicar la cantidad de páginas",
    })
    .min(1, "La cantidad de páginas debe ser positiva"),

  personaId: z
    .number({
      required_error: "Debe indicar la persona dueña del libro",
      invalid_type_error: "Debe indicar la persona dueña del libro",
    }),

  pdfPath: z.string().optional().nullable(),
});
export type LibroSummaryDto = z.infer<typeof LibroSummaryDto>;

export const LibroDetailDto = LibroSummaryDto;
export type LibroDetailDto = z.infer<typeof LibroCreateDto>;