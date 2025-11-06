import { Badge, BadgeProps } from '@mantine/core';
import { Autor } from '@/api/entities/autores';
import { match } from '@/utilities/match';

interface AutorStatusBadgeProps extends Omit<BadgeProps, 'children' | 'color'> {
  status: Autor['status'];
}

export function AutorStatusBadge({
  status,
  variant = 'outline',
  ...props
}: AutorStatusBadgeProps) {
  const color = match(
    [status === 'active', 'teal'],
    [status === 'banned', 'orange'],
    [status === 'archived', 'red'],
    [true, 'gray']
  );

  return (
    <Badge color={color} variant={variant} {...props}>
      {status}
    </Badge>
  );
}
