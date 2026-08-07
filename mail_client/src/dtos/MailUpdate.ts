import { AttachmentResponse } from './AttachmentResponse';

/**
 * DTO for updating an existing mail.
 */
export type MailUpdate = {
  mailId: string;
  to?: string[];
  cc?: string[];
  bcc?: string[];
  replyTo?: string[];
  subject?: string;
  bodyText?: string;
  attachments?: AttachmentResponse[];
};
