import {Attachment} from '../models/Attachment';

//Todo warum hat das hier kein reply-to

/**
 * Data Transfer Object for updating a Mail
 * @property {string} mailId The ID of the Mail to update
 * @property {string[]?} to The Mail Addresses of the recipients
 * @property {string[]?} cc The Mail Addresses of the Carbon Copy recipients
 * @property {string[]?} bcc The Mail Addresses of the blind Carbon Copy recipients
 * @property {string?} subject The Subject of the Mail
 * @property {string?} bodyText The body of the Mail
 * @property {Attachment[]?} attachments The attachments of the Mail
 */
export type MailUpdate = {
  mailId: string,
  to?: string [],
  cc?: string [],
  bcc?: string [],
  subject?: string,
  bodyText?: string,
  attachments?: Attachment[]
}
