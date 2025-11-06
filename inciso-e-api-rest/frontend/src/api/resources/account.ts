import { client } from '../axios';
import { UsuarioDetailDto } from '../dtos';

export async function getAccountInfo() {
  const response = await client.get('auth/me');
  return UsuarioDetailDto.parse(response.data);
}
