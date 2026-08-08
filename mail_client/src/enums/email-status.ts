/**
 * Enum for the status of a mail.
 *
 * @property {string} DRAFT - The mail is a draft.
 * @property {string} SENT - The mail has been sent.
 * @property {string} RECEIVED - The mail has been received.
 * @property {string} ERROR - An error has occurred while sending the mail
 */
export enum EmailStatus {
  DRAFT = 'DRAFT',
  SENT = 'SENT',
  RECEIVED = 'RECEIVED',
  ERROR = 'ERROR'
}
