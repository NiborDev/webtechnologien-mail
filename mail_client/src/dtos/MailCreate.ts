import {Attachment} from '../models/Attachment';

/**
 * Data Transfer Object for creating a Mail
 * @property {string[]} to The Mail Addresses of the recipients
 * @property {string[]} cc The Mail Addresses of the Carbon Copy recipients
 * @property {string[]} bcc The Mail Addresses of the blind Carbon Copy recipients
 * @property {string[]} replyTo separate Mail Addresses for replies
 * @property {string} subject The Subject of the Mail
 * @property {string} bodyText The body of the Mail
 * @property {Attachment[]} attachments The attachments of the Mail
 */
export type MailCreate = {
  to: string [],
  cc: string [],
  bcc: string [],
  replyTo: string [],
  subject: string,
  bodyText: string,
  attachments: Attachment[]
}
