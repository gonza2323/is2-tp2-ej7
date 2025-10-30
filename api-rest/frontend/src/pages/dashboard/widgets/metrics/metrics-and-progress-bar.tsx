import { Group } from '@mantine/core';
import { MetricCard } from '@/components/metric-card';
import { formatInt, formatPercentage } from '@/utilities/number';

const metrics = [
  {
    title: 'Total images',
    value: 44012,
    color: 'var(--mantine-color-teal-6)',
    trend: 14.44,
    percentage: 54,
  },
  {
    title: 'Total videos',
    value: 440612,
    color: 'var(--mantine-color-red-6)',
    trend: -18.45,
    percentage: 48,
  },
  {
    title: 'Total Documents',
    value: 90875,
    color: 'var(--mantine-color-orange-6)',
    trend: 20.34,
    percentage: 80.3,
  },
  {
    title: 'Total videos',
    value: 63778,
    color: 'var(--mantine-color-blue-6)',
    trend: 14.44,
    percentage: 54,
  },
];

export function MetricsAndProgressBar() {
  return metrics.map(({ title, value, color, trend, percentage }) => (
    <MetricCard.Root key={title}>
      <Group justify="space-between" wrap="nowrap">
        <div>
          <MetricCard.TextMuted>{title}</MetricCard.TextMuted>
          <MetricCard.TextEmphasis mb="sm">
            {formatInt(value, { suffix: ' GB' })}
          </MetricCard.TextEmphasis>
          <MetricCard.TextTrend value={trend}>last month</MetricCard.TextTrend>
        </div>

        <MetricCard.RingProgress
          size={92}
          label={formatPercentage(percentage, { precision: 0 })}
          sections={[{ value: percentage, color }]}
        />
      </Group>
    </MetricCard.Root>
  ));
}
