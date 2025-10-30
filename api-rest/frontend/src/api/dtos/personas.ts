import { z } from 'zod';

export const DomicilioCreateDto = z.object({
  id: z.number().optional(),
  calle: z
    .string({
      required_error: "Debe indicar la calle",
    })
    .min(1, "Debe indicar la calle")
    .max(50, "Máximo 50 caracteres"),
  numeracion: z
    .string({ required_error: "Debe indicar la numeración" })
    .min(1, "Debe indicar la numeración")
    .transform((val) => Number(val)),
  localidadId: z
    .string({ required_error: "Debe seleccionar una localidad" })
    .min(1, "Debe seleccionar una localidad")
    .transform((val) => Number(val)),
});

export type DomicilioCreateDto = z.infer<typeof DomicilioCreateDto>;

export const DomicilioUpdateDto = DomicilioCreateDto;
export type DomicilioUpdateDto = z.infer<typeof DomicilioUpdateDto>;

export const DomicilioDetailDto = z.object({
  id: z.number(),
  calle: z.string(),
  numeracion: z.number(),
  localidadId: z.number(),
});
export type DomicilioDetailDto = z.infer<typeof DomicilioDetailDto>;

export const DomicilioSummaryDto = DomicilioDetailDto;
export type DomicilioSummaryDto = z.infer<typeof DomicilioSummaryDto>;


export const PersonaCreateDto = z.object({
  id: z.number().optional(),
  nombre: z
    .string({
      required_error: "El nombre no puede estar vacío",
    })
    .min(1, "El nombre no puede estar vacío")
    .max(50, "Máximo 50 caracteres"),
  apellido: z
    .string({
      required_error: "El apellido no puede estar vacío",
    })
    .min(1, "El apellido no puede estar vacío")
    .max(50, "Máximo 50 caracteres"),
  dni: z
    .string({ required_error: "Debe indicar el DNI" })
    .min(1, "Debe indicar el DNI")
    .transform((val) => Number(val)),
  domicilio: DomicilioCreateDto, 
});

export type PersonaCreateDto = z.infer<typeof PersonaCreateDto>;

export const PersonaUpdateDto = PersonaCreateDto;
export type PersonaUpdateDto = z.infer<typeof PersonaUpdateDto>;

export const PersonaDetailDto = z.object({
  id: z.number(),
  nombre: z.string(),
  apellido: z.string(),
  dni: z.number(),
  domicilio: DomicilioDetailDto,
});
export type PersonaDetailDto = z.infer<typeof PersonaDetailDto>;

export const PersonaSummaryDto = z.object({
  id: z.number(),
  nombre: z.string(),
  apellido: z.string(),
  dni: z.number(),
  domicilio: DomicilioSummaryDto,
});
export type PersonaSummaryDto = z.infer<typeof PersonaSummaryDto>;