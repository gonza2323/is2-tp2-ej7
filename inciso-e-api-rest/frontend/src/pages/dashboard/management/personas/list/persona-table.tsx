import { useEffect, useMemo } from 'react';
import { DataTableColumn } from 'mantine-datatable';
import { Avatar, Box, Group, Loader, Rating, Text } from '@mantine/core';
import { PersonaSummaryDto } from '@/api/dtos';
import { usePagination } from '@/api/helpers';
import { AddButton } from '@/components/add-button';
import { DataTable } from '@/components/data-table';
import { LinkChip } from '@/components/link-chip';
import { useGetPersonas, useDeletePersona } from '@/hooks';
import { paths } from '@/routes';
import { formatDate } from '@/utilities/date';
import { formatPhoneNumber } from '@/utilities/phone-number';
import { firstLetters } from '@/utilities/text';
import { NavLink, useNavigate } from 'react-router-dom';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';

type SortableFields = PersonaSummaryDto;

export function PersonasTable() {
  const navigate = useNavigate();
  const { page, size, setSize: setSize, setPage } = usePagination();
  // const { tabs, filters, sort } = DataTable.useDataTable<SortableFields>({
  const { sort } = DataTable.useDataTable<SortableFields>({
    sortConfig: {
      direction: 'asc',
      column: 'dni',
    },
  });

  const { data, isLoading } = useGetPersonas({
    query: {
      page,
      size,
      // status: tabs.value as Persona['status'],
      sort: sort.query,
    },
  });

  useEffect(() => {
    const totalPages = data?.meta?.totalPages;
    if (totalPages != null && page != null) {
      if (page >= totalPages) {
        setPage(Math.max(page - 1, 0));
      }
    }
  }, [data, page, setPage]);

  const deleteMutation = useDeletePersona();

  const columns: DataTableColumn<PersonaSummaryDto>[] = useMemo(
    () => [
      {
        accessor: 'dni',
        title: 'DNI',
        sortable: true,
        render: (persona) => (
          <Text truncate="end">{persona.dni}</Text>
        ),
      },
      {
        accessor: 'apellido',
        title: 'Apellido',
        sortable: true,
        render: (persona) => (
          <Text truncate="end">{persona.apellido}</Text>
        ),
      },
      {
        accessor: 'nombre',
        title: 'Nombre',
        sortable: true,
        render: (persona) => (
          <Text truncate="end">{persona.nombre}</Text>
        ),
      },
      {
        accessor: 'actions',
        title: 'Acciones',
        textAlign: 'right',
        width: 100,
        render: (persona) => (
          <DataTable.Actions
            onView={() => { navigate(paths.dashboard.management.personas.view(persona.id)) }}
            onEdit={() => { navigate(paths.dashboard.management.personas.edit(persona.id)) }}
            onDelete={() => handleDelete(persona)}
          />
        ),
      },
    ],
    []
  );

  const handleDelete = (persona: PersonaSummaryDto) => {
    modals.openConfirmModal({
      title: 'Confirmar borrado',
      children: <Text>¿Está seguro de que desea borrar el persona?</Text>,
      labels: { confirm: 'Delete', cancel: 'Cancel' },
      confirmProps: { color: 'red' },
      onConfirm: () => {
        deleteMutation.mutate({
          model: persona,
          route: { id: persona.id },
        });
      },
    });
  };

  return (
    <DataTable.Container>
      <DataTable.Title
        title="Personas"
        description="Lista de personas"
        actions={
          <AddButton variant="default" size="xs" component={NavLink} to={paths.dashboard.management.personas.add}>
            Agregar persona
          </AddButton>
        }
      />

      {/* <DataTable.Tabs tabs={tabs.tabs} onChange={tabs.change} /> */}
      {/* <DataTable.Filters filters={filters.filters} onClear={filters.clear} /> */}
      <DataTable.Content>
        <DataTable.Table
          minHeight={240}
          noRecordsText={DataTable.noRecordsText('persona')}
          recordsPerPageLabel={DataTable.recordsPerPageLabel('personas')}
          paginationText={DataTable.paginationText('personas')}
          page={page + 1}
          records={data?.data ?? []}
          fetching={isLoading}
          onPageChange={(pageNo) => setPage(pageNo - 1)}
          recordsPerPage={size}
          totalRecords={data?.meta.totalElements ?? 0}
          onRecordsPerPageChange={setSize}
          recordsPerPageOptions={[5, 15, 30]}
          sortStatus={sort.status}
          onSortStatusChange={sort.change}
          columns={columns}
        />
      </DataTable.Content>
    </DataTable.Container>
  );
}
