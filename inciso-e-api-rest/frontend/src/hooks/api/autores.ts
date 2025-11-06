import { AutorDetailDto, AutorSummaryDto, AutorCreateDto } from '@/api/dtos';
import { createDeleteMutationHook, createGetQueryHook, createPaginationQueryHook, createPostMutationHook, SortableQueryParams } from '@/api/helpers';
import { notifications } from '@mantine/notifications';
import { z } from 'zod';

const QUERY_KEY = 'autores';
const BASE_ENDPOINT = 'autores';

export const useGetAutor = createGetQueryHook({
  endpoint: `${BASE_ENDPOINT}/:id`,
  responseSchema: AutorDetailDto,
  rQueryParams: { queryKey: [QUERY_KEY] },
});

export const useGetAutores = createPaginationQueryHook<
  typeof AutorSummaryDto,
  SortableQueryParams
>({
  endpoint: BASE_ENDPOINT,
  dataSchema: AutorSummaryDto,
  rQueryParams: { queryKey: [QUERY_KEY] },
});

export const useGetAutoresFull = createGetQueryHook({
  endpoint: `${BASE_ENDPOINT}/all`,
  responseSchema: z.array(AutorSummaryDto),
  rQueryParams: { queryKey: [QUERY_KEY] },
});

export const useCreateAutor = createPostMutationHook({
  endpoint: BASE_ENDPOINT,
  bodySchema: AutorCreateDto,
  responseSchema: AutorDetailDto,
});

export const useDeleteAutor = createDeleteMutationHook<
  { id: number }
>({
  endpoint: `${BASE_ENDPOINT}/:id`,
  rMutationParams: {
    onSuccess: (data, variables, context, queryClient) => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY] });
      notifications.show({
        title: 'Borrado',
        message: 'El autor fue borrada con éxito',
        color: 'green',
      });
    },
    onError: (error) => {
      notifications.show({
        title: 'Error',
        message: error.message || 'No se pudo borrar el autor',
        color: 'red',
      });
    },
  },
});
