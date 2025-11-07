import { LibroDetailDto, LibroSummaryDto, LibroCreateDto } from '@/api/dtos';
import { createDeleteMutationHook, createGetQueryHook, createPaginationQueryHook, createPostMutationHook, SortableQueryParams } from '@/api/helpers';
import { notifications } from '@mantine/notifications';

const QUERY_KEY = 'libros';
const BASE_ENDPOINT = 'libros';

export const useGetLibro = createGetQueryHook({
  endpoint: `${BASE_ENDPOINT}/:id`,
  responseSchema: LibroDetailDto,
  rQueryParams: { queryKey: [QUERY_KEY] },
});

export const useGetLibros = createPaginationQueryHook<
  typeof LibroSummaryDto,
  SortableQueryParams
>({
  endpoint: BASE_ENDPOINT,
  dataSchema: LibroSummaryDto,
  rQueryParams: { queryKey: [QUERY_KEY] },
});

export const useCreateLibro = createPostMutationHook({
  endpoint: BASE_ENDPOINT,
  bodySchema: LibroCreateDto,
  responseSchema: LibroDetailDto,
});

export const useDeleteLibro = createDeleteMutationHook<
  { id: number }
>({
  endpoint: `${BASE_ENDPOINT}/:id`,
  rMutationParams: {
    onSuccess: (data, variables, context, queryClient) => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY] });
      notifications.show({
        title: 'Borrado',
        message: 'El libro fue borrada con éxito',
        color: 'green',
      });
    },
    onError: (error) => {
      notifications.show({
        title: 'Error',
        message: error.message || 'No se pudo borrar el libro',
        color: 'red',
      });
    },
  },
});
