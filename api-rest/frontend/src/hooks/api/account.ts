import { UsuarioDetailDto } from '@/api/dtos';
import { createGetQueryHook } from '@/api/helpers';

export const useGetAccountInfo = createGetQueryHook({
  endpoint: '/account',
  responseSchema: UsuarioDetailDto,
  rQueryParams: { queryKey: ['account'] },
});
