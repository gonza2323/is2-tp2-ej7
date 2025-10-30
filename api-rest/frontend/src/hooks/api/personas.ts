import { PersonaDetailDto, PersonaSummaryDto, PersonaCreateDto } from '@/api/dtos';
import { createDeleteMutationHook, createGetQueryHook, createPaginationQueryHook, createPostMutationHook, SortableQueryParams } from '@/api/helpers';
import { notifications } from '@mantine/notifications';
import { z } from 'zod';

const QUERY_KEY = 'personas';
const BASE_ENDPOINT = 'personas';

export const useGetPersona = createGetQueryHook({
  endpoint: `${BASE_ENDPOINT}/:id`,
  responseSchema: PersonaDetailDto,
  rQueryParams: { queryKey: [QUERY_KEY] },
});

export const useGetPersonas = createPaginationQueryHook<
  typeof PersonaSummaryDto,
  SortableQueryParams
>({
  endpoint: BASE_ENDPOINT,
  dataSchema: PersonaSummaryDto,
  rQueryParams: { queryKey: [QUERY_KEY] },
});

export const useGetPersonasFull = createGetQueryHook({
  endpoint: `${BASE_ENDPOINT}/all`,
  responseSchema: z.array(PersonaSummaryDto),
  rQueryParams: { queryKey: [QUERY_KEY] },
});

export const useCreatePersona = createPostMutationHook({
  endpoint: BASE_ENDPOINT,
  bodySchema: PersonaCreateDto,
  responseSchema: PersonaDetailDto,
});

export const useDeletePersona = createDeleteMutationHook<
  { id: number }
>({
  endpoint: `${BASE_ENDPOINT}/:id`,
  rMutationParams: {
    onSuccess: (data, variables, context, queryClient) => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY] });
      notifications.show({
        title: 'Borrado',
        message: 'La persona fue borrada con éxito',
        color: 'green',
      });
    },
    onError: (error) => {
      notifications.show({
        title: 'Error',
        message: error.message || 'No se pudo borrar la persona',
        color: 'red',
      });
    },
  },
});
