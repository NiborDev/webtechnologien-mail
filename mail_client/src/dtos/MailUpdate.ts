import { AttachmentResponse } from './AttachmentResponse';

/**
 * DTO for updating an existing mail.
 *
 * @property mailId - ID of the mail to update
 * @property to - array of recipient email addresses
 * @property cc - array of CC recipient email addresses
 * @property bcc - array of BCC recipient email addresses
 * @property replyTo - array of reply-to email addresses
 * @property subject - subject of the mail
 * @property bodyText - body text of the mail
 * @property attachments - array of attachments
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
