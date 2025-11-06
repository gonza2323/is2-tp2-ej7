import { z } from 'zod';

export const ProveedorCreateDto = z.object({
  nombre: z.string().nonempty ("El nombre no puede estar vacío"),
});
export type ProveedorCreateDto = z.infer<typeof ProveedorCreateDto>;

export const ProveedorDetailDto = z.object({
  id: z.number(),
  nombre: z.string().min(1),
});
export type ProveedorDetailDto = z.infer<typeof ProveedorDetailDto>;

export const ProveedorSummaryDto = ProveedorDetailDto;
export type ProveedorSummaryDto = z.infer<typeof ProveedorSummaryDto>;

export const ProveedorUpdateDto = ProveedorDetailDto;
export type ProveedorUpdateDto = z.infer<typeof ProveedorUpdateDto>;
