import { AttachmentResponse } from './AttachmentResponse';

/**
 * DTO for creating a new mail.
 */
export type MailCreate = {
  to: string[];
  cc: string[];
  bcc: string[];
  replyTo: string[];
  subject: string;
  bodyText: string;
  attachments: AttachmentResponse[];
};
