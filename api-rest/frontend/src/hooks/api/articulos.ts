import { ArticuloDetailDto, ArticuloSummaryDto, ArticuloCreateDto } from '@/api/dtos';
import { createDeleteMutationHook, createGetQueryHook, createPaginationQueryHook, createPostMutationHook, SortableQueryParams } from '@/api/helpers';
import { notifications } from '@mantine/notifications';

const QUERY_KEY = 'articulos';
const BASE_ENDPOINT = 'articulos';

export const useGetArticulo = createGetQueryHook({
  endpoint: `${BASE_ENDPOINT}/:id`,
  responseSchema: ArticuloDetailDto,
  rQueryParams: { queryKey: [QUERY_KEY] },
});

export const useGetArticulos = createPaginationQueryHook<
  typeof ArticuloSummaryDto,
  SortableQueryParams
>({
  endpoint: BASE_ENDPOINT,
  dataSchema: ArticuloSummaryDto,
  rQueryParams: { queryKey: [QUERY_KEY] },
});

export const useCreateArticulo = createPostMutationHook({
  endpoint: BASE_ENDPOINT,
  bodySchema: ArticuloCreateDto,
  responseSchema: ArticuloDetailDto,
});

export const useDeleteArticulo = createDeleteMutationHook<
  { id: number }
>({
  endpoint: `${BASE_ENDPOINT}/:id`,
  rMutationParams: {
    onSuccess: (data, variables, context, queryClient) => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY] });
      notifications.show({
        title: 'Borrado',
        message: 'El artículo fue borrado con éxito',
        color: 'green',
      });
    },
    onError: (error) => {
      notifications.show({
        title: 'Error',
        message: error.message || 'No se pudo borrar el artículo',
        color: 'red',
      });
    },
  },
});
