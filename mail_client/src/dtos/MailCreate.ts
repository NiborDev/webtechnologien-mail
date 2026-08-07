import {Attachment} from '../models/Attachment';

export type MailCreate = {
  to: string [],
  cc: string [],
  bcc: string [],
  replyTo: string [],
  subject: string,
  bodyText: string,
  attachments: Attachment[]
}
