import { z } from 'zod';

export const ArticuloCreateDto = z.object({
  nombre: z.string().min(1).max(50),
  precio: z.number().min(0),
  proveedorId: z.number().optional(),
  imagenArchivo: z
    .instanceof(File)
    .optional()
    .nullable(),
});
export type ArticuloCreateDto = z.infer<typeof ArticuloCreateDto>;


export const ArticuloSummaryDto = z.object({
  id: z.number(),
  nombre: z.string().min(1).max(50),
  precio: z.number().min(0),
  proveedorNombre: z.string().min(1),
  imagenId: z.number(),
});
export type ArticuloSummaryDto = z.infer<typeof ArticuloSummaryDto>;


export const ArticuloDetailDto = z.object({
  id: z.number(),
  nombre: z.string().min(1).max(50),
  precio: z.number().min(0),
  proveedorId: z.number().optional(),
  proveedorNombre: z.string().min(1),
  imagenId: z.number(),
});
export type ArticuloDetailDto = z.infer<typeof ArticuloDetailDto>;

export const ArticuloUpdateDto = ArticuloDetailDto;
export type ArticuloUpdateDto = z.infer<typeof ArticuloUpdateDto>;
