/**
 * A Helper function to format dates.
 * If the date is today, it will be formatted as time (e.g. '14:30').
 * If the date is from the current year, it will be formatted as day and short month (e.g. '4 Aug').
 * Otherwise, if the date is from a different year, it will be formatted with the year (e.g. '4 Aug 2025').
 *
 * @param date The date to format.
 * @returns The formatted date string.
 */
export function formatDateDisplay(date: Date | null | undefined): string {
  if (!date || isNaN(date.getTime())) return '';

  const now = new Date();
  const isToday =
    date.getDate() === now.getDate() &&
    date.getMonth() === now.getMonth() &&
    date.getFullYear() === now.getFullYear();

  if (isToday) {
    return date.toLocaleTimeString(undefined, { hour: '2-digit', minute: '2-digit' });
  }

  const isCurrentYear = date.getFullYear() === now.getFullYear();
  if (isCurrentYear) {
    return date.toLocaleDateString(undefined, { day: 'numeric', month: 'short' });
  }

  return date.toLocaleDateString(undefined, { day: 'numeric', month: 'short', year: 'numeric' });
}
