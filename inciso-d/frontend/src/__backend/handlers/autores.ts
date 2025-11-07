import { http, HttpResponse } from 'msw';
import { database } from '@/__backend/database';
import { apiUrl, paginate, sort } from '@/__backend/helpers';
import { app } from '@/config';
import { pipe } from '@/utilities/pipe';

export default [
  // GET /autores
  http.get(apiUrl('/autores'), ({ request }) => {
    const url = new URL(request.url);

    const status = url.searchParams.get('status');

    return HttpResponse.json(
      pipe(
        database.autores,
        (allAutores) =>
          status && status !== '*'
            ? allAutores.filter((autor) => autor.status === status)
            : allAutores,
        (filteredAutores) =>
          sort(request, filteredAutores, [
            { name: 'fullName', type: 'string' },
            { name: 'rating', type: 'number' },
            { name: 'createdAt', type: 'date' },
          ]),
        (sortedAutores) => paginate(request, sortedAutores)
      )
    );
  }),

  // GET /autores/metrics
  http.get(`${app.apiBaseUrl}/autores/metrics`, () =>
    HttpResponse.json({
      total: database.autores.length,
      active: database.autores.filter((autor) => autor.status === 'active').length,
      banned: database.autores.filter((autor) => autor.status === 'banned').length,
      archived: database.autores.filter((autor) => autor.status === 'archived').length,
    })
  ),
];
