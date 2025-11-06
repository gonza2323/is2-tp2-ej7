import { ProveedorDetailDto, ProveedorSummaryDto, ProveedorCreateDto } from '@/api/dtos';
import { createDeleteMutationHook, createGetQueryHook, createPaginationQueryHook, createPostMutationHook, SortableQueryParams } from '@/api/helpers';
import { notifications } from '@mantine/notifications';

const QUERY_KEY = 'proveedores';
const BASE_ENDPOINT = 'proveedores';

export const useGetProveedor = createGetQueryHook({
  endpoint: `${BASE_ENDPOINT}/:id`,
  responseSchema: ProveedorDetailDto,
  rQueryParams: { queryKey: [QUERY_KEY] },
});

export const useGetProveedores = createPaginationQueryHook<
  typeof ProveedorSummaryDto,
  SortableQueryParams
>({
  endpoint: BASE_ENDPOINT,
  dataSchema: ProveedorSummaryDto,
  rQueryParams: { queryKey: [QUERY_KEY] },
});

export const useCreateProveedor = createPostMutationHook({
  endpoint: BASE_ENDPOINT,
  bodySchema: ProveedorCreateDto,
  responseSchema: ProveedorDetailDto,
});

export const useDeleteProveedor = createDeleteMutationHook<
  { id: number }
>({
  endpoint: `${BASE_ENDPOINT}/:id`,
  rMutationParams: {
    onSuccess: (data, variables, context, queryClient) => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY] });
      notifications.show({
        title: 'Borrado',
        message: 'El proveedor fue borrado con éxito',
        color: 'green',
      });
    },
    onError: (error) => {
      notifications.show({
        title: 'Error',
        message: error.message || 'No se pudo borrar el proveedor',
        color: 'red',
      });
    },
  },
});
