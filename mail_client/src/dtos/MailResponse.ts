import { EmailStatus } from '../enums/email-status';
import { Attachment } from './Attachment';

/**
 * Data Transfer Object for a mail.
 * @property {string} mailId - The unique identifier of the mail.
 * @property {string} from - The sender of the mail.
 * @property {string[]} to - The recipients of the mail.
 * @property {string[]} cc - The carbon copy recipients of the mail.
 * @property {string[]} bcc - The blind carbon copy recipients of the mail.
 * @property {string[]} replyTo - The reply-to recipients of the mail.
 * @property {string} subject - The subject of the mail.
 * @property {string} bodyText - The body of the mail.
 * @property {EmailStatus} status - The status of the mail.
 * @property {Attachment[]} attachments - The attachments of the mail.
 * @property {boolean} isRead - Whether the mail is read.
 * @property {boolean} isMarked - Whether the mail is marked.
 * @property {string | null} sentAt - The sent date of the mail.
 * @property {string | null} receivedAt - The received date of the mail.
 * @property {string} createdAt - The creation date of the mail.
 * @property {string} updatedAt - The update date of the mail.
 * @property {string} source - The source of the mail.
 */
export interface MailResponse {
  mailId: string;
  from: string;
  to: string[];
  cc: string[];
  bcc: string[];
  replyTo: string[];
  subject: string;
  bodyText: string;
  status: EmailStatus;
  attachments: Attachment[];
  isRead: boolean;
  isMarked: boolean;
  sentAt?: string | null;
  receivedAt?: string | null;
  createdAt: string;
  updatedAt: string;
  source?: string;
}
