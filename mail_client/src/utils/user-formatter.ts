/**
 * Formats the sender name from an email address to the user's name.
 * If the email is not found in the user map, the email address is returned.
 *
 * @param from The sender's email address.
 * @param userMap A map of email addresses to user names.
 * @returns The formatted sender name.
 */
export function formatSenderName(from: string, userMap?: Map<string, string>): string {
  if (!from) return '';
  const cleanEmail = from.toLowerCase().trim();
  return userMap?.get(cleanEmail) || from;
}

/**
 * Formats the recipient names from an array of email addresses to user names.
 * If the email is not found in the user map, the email address is returned.
 *
 * @param recipients The array of recipient email addresses.
 * @param userMap A map of email addresses to user names.
 * @returns The formatted recipient names.
 */
export function formatRecipientNames(recipients: string[], userMap?: Map<string, string>): string {
  if (!recipients || recipients.length === 0) return '(No Recipients)';
  return recipients
    .map((email) => userMap?.get(email.toLowerCase().trim()) || email)
    .join(', ');
}
