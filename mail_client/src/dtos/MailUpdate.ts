import {Attachment} from '../models/Attachment';

export type MailUpdate = {
  mailId: string,
  from: string,
  to?: string [],
  cc?: string [],
  bcc?: string [],
  subject?: string,
  bodyText?: string,
  attachments?: Attachment[]
}
