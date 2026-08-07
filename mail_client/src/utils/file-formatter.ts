/**
 * Formats bytes into a readable string (e.g., 1.5 KB, 3.2 MB).
 *
 * @param bytes The number of bytes.
 * @param decimals The number of decimal places to display. Defaults to 1.
 * @returns The formatted string representation of bytes.
 */
export function formatBytes(bytes: number, decimals = 1): string {
  if (!bytes || bytes === 0) return '0 B';

  const bytesPerKilobyte = 1024;
  const decimalPlaces = decimals < 0 ? 0 : decimals;
  const sizeUnits = ['B', 'KB', 'MB', 'GB', 'TB'];

  const exponent = Math.floor(Math.log(bytes) / Math.log(bytesPerKilobyte));
  const unitIndex = Math.min(exponent, sizeUnits.length - 1);

  const formattedValue = parseFloat(
    (bytes / Math.pow(bytesPerKilobyte, unitIndex)).toFixed(decimalPlaces),
  );

  return `${formattedValue} ${sizeUnits[unitIndex]}`;
}
