import {EMailStatus} from '../enums/email-status';
import {Attachment} from '../models/Attachment';
import {Source} from '../enums/source';

export type MailResponse = {
  mailId: string,
  from: string,
  to: string[],
  cc: string[],
  bcc: string[],
  replyTo: string[],
  subject: string,
  bodyText: string,
  status: EMailStatus,
  attachments: Attachment[],
  isRead: boolean,
  isMarked: boolean,
  sentAt: Date | null,
  receivedAt: Date | null,
  source: Source,
}
