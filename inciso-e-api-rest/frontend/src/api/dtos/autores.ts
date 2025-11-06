import { z } from 'zod';

export const AutorCreateDto = z.object({
  id: z.number().optional(),
  nombre: z
    .string({
      required_error: "El nombre no puede estar vacío",
    })
    .min(1, "El nombre no puede estar vacío"),
  apellido: z
    .string({
      required_error: "El apellido no puede estar vacío",
    })
    .min(1, "El apellido no puede estar vacío"),
  biografia: z.string().optional().nullable(),
});

export type AutorCreateDto = z.infer<typeof AutorCreateDto>;

export const AutorUpdateDto = AutorCreateDto;
export type AutorUpdateDto = z.infer<typeof AutorCreateDto>;

export const AutorDetailDto = AutorCreateDto;
export type AutorDetailDto = z.infer<typeof AutorCreateDto>;

export const AutorSummaryDto = AutorCreateDto;
export type AutorSummaryDto = z.infer<typeof AutorCreateDto>;