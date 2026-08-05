/**
 * Truncates the text to a specified maximum length.
 * If the text is longer than the maximum length, it will be truncated and '...' will be appended.
 * If the text is below or equal to the maximum length, it will be returned as it is.
 *
 * @param text The text to truncate.
 * @param maxLength The maximum length of the text. Defaults to 90.
 * @returns The truncated text, or the original text if it is shorter than or equal to the maximum length.
 */
export function truncateText(text: string, maxLength = 90): string {
  if (!text) return '';
  return text.length > maxLength ? text.substring(0, maxLength) + '...' : text;
}
