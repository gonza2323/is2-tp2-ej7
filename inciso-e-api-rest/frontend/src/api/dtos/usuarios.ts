import { z } from 'zod';

export const UsuarioDetailDto = z.object({
  id: z.number().int(),
  email: z.string().min(1),
  rol: z.string().min(1),
});

export type UsuarioDetailDto = z.infer<typeof UsuarioDetailDto>;
