import { LocalidadDetailDto, LocalidadSummaryDto, LocalidadCreateDto } from '@/api/dtos';
import { createDeleteMutationHook, createGetQueryHook, createPaginationQueryHook, createPostMutationHook, SortableQueryParams } from '@/api/helpers';
import { notifications } from '@mantine/notifications';
import { z } from 'zod';

const QUERY_KEY = 'localidades';
const BASE_ENDPOINT = 'localidades';

export const useGetLocalidad = createGetQueryHook({
  endpoint: `${BASE_ENDPOINT}/:id`,
  responseSchema: LocalidadDetailDto,
  rQueryParams: { queryKey: [QUERY_KEY] },
});

export const useGetLocalidadesFull = createGetQueryHook({
  endpoint: `${BASE_ENDPOINT}/all`,
  responseSchema: z.array(LocalidadSummaryDto),
  rQueryParams: { queryKey: [QUERY_KEY] },
});

export const useGetLocalidades = createPaginationQueryHook<
  typeof LocalidadSummaryDto,
  SortableQueryParams
>({
  endpoint: BASE_ENDPOINT,
  dataSchema: LocalidadSummaryDto,
  rQueryParams: { queryKey: [QUERY_KEY] },
});

export const useCreateLocalidad = createPostMutationHook({
  endpoint: BASE_ENDPOINT,
  bodySchema: LocalidadCreateDto,
  responseSchema: LocalidadDetailDto,
});

export const useDeleteLocalidad = createDeleteMutationHook<
  { id: number }
>({
  endpoint: `${BASE_ENDPOINT}/:id`,
  rMutationParams: {
    onSuccess: (data, variables, context, queryClient) => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY] });
      notifications.show({
        title: 'Borrado',
        message: 'La localidad fue borrada con éxito',
        color: 'green',
      });
    },
    onError: (error) => {
      notifications.show({
        title: 'Error',
        message: error.message || 'No se pudo borrar la localidad',
        color: 'red',
      });
    },
  },
});
