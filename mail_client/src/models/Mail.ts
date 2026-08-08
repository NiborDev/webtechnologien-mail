import { MailResponse } from '../dtos/MailResponse';
import { Attachment } from './Attachment';
import { EmailStatus } from '../enums/email-status';
import { formatDateDisplay } from '../utils/date-formatter';
import { truncateText } from '../utils/text-formatter';

/**
 * Model for a mail.
 *
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
 * @property {Date | null} sentAt - The sent date of the mail.
 * @property {Date | null} receivedAt - The received date of the mail.
 * @property {Date} createdAt - The creation date of the mail.
 * @property {Date} updatedAt - The update date of the mail.
 */
export class Mail {
  /**
   * Constructs a Mail instance.
   *
   * @property {string} _mailId - The unique identifier of the mail.
   * @property {string} _from - The sender of the mail.
   * @property {string[]} _to - The recipients of the mail.
   * @property {string[]} _cc - The carbon copy recipients of the mail.
   * @property {string[]} _bcc - The blind carbon copy recipients of the mail.
   * @property {string[]} _replyTo - The reply-to recipients of the mail.
   * @property {string} _subject - The subject of the mail.
   * @property {string} _bodyText - The body of the mail.
   * @property {EmailStatus} _status - The status of the mail.
   * @property {Attachment[]} _attachments - The attachments of the mail.
   * @property {boolean} _isRead - Whether the mail is read.
   * @property {boolean} _isMarked - Whether the mail is marked.
   * @property {Date | null} _sentAt - The sent date of the mail.
   * @property {Date | null} _receivedAt - The received date of the mail.
   * @property {Date} _createdAt - The creation date of the mail.
   * @property {Date} _updatedAt - The update date of the mail.
   */
  constructor(
    private _mailId: string,
    private _from: string,
    private _to: string[],
    private _cc: string[],
    private _bcc: string[],
    private _replyTo: string[],
    private _subject: string,
    private _bodyText: string,
    private _status: EmailStatus,
    private _attachments: Attachment[],
    private _isRead: boolean,
    private _isMarked: boolean,
    private _sentAt: Date | null,
    private _receivedAt: Date | null,
    private _createdAt: Date,
    private _updatedAt: Date,
  ) {}

  /**
   * Factory method to create a Mail from a MailResponse.
   *
   * @param dto The MailResponse to convert.
   * @returns A new Mail instance.
   */
  public static fromDto(dto: MailResponse): Mail {
    return new Mail(
      dto.mailId,
      dto.from || '',
      dto.to || [],
      dto.cc || [],
      dto.bcc || [],
      dto.replyTo || [],
      dto.subject || '(No Subject)',
      dto.bodyText || '',
      dto.status,
      (dto.attachments || []).map((att) => Attachment.fromDto(att)),
      dto.isRead,
      dto.isMarked,
      dto.sentAt ? new Date(dto.sentAt) : null,
      dto.receivedAt ? new Date(dto.receivedAt) : null,
      new Date(dto.createdAt),
      new Date(dto.updatedAt),
    );
  }

  /**
   * Converts this Mail model instance to a MailResponse DTO.
   *
   * @returns A MailResponse DTO.
   */
  public toDto(): MailResponse {
    return {
      mailId: this._mailId,
      from: this._from,
      to: this._to,
      cc: this._cc,
      bcc: this._bcc,
      replyTo: this._replyTo,
      subject: this._subject,
      bodyText: this._bodyText,
      status: this._status,
      attachments: this._attachments.map((att) => att.toDto()),
      isRead: this._isRead,
      isMarked: this._isMarked,
      sentAt: this._sentAt ? this._sentAt.toISOString() : null,
      receivedAt: this._receivedAt ? this._receivedAt.toISOString() : null,
      createdAt: this._createdAt.toISOString(),
      updatedAt: this._updatedAt.toISOString(),
    };
  }

  /**
   * Gets the mail ID.
   * @returns The mail ID.
   */
  public get mailId(): string {
    return this._mailId;
  }

  /**
   * Gets the sender of the mail.
   * @returns The sender of the mail.
   */
  public get from(): string {
    return this._from;
  }

  /**
   * Gets the recipients of the mail.
   * @returns The recipients of the mail.
   */
  public get to(): string[] {
    return this._to;
  }

  /**
   * Gets the carbon copy recipients of the mail.
   * @returns The carbon copy recipients of the mail.
   */
  public get cc(): string[] {
    return this._cc;
  }

  /**
   * Gets the blind carbon copy recipients of the mail.
   * @returns The blind carbon copy recipients of the mail.
   */
  public get bcc(): string[] {
    return this._bcc;
  }

  /**
   * Gets the reply-to recipients of the mail.
   * @returns The reply-to recipients of the mail.
   */
  public get replyTo(): string[] {
    return this._replyTo;
  }

  /**
   * Gets the subject of the mail.
   * @returns The subject of the mail.
   */
  public get subject(): string {
    return this._subject;
  }

  /**
   * Gets the body text of the mail.
   * @returns The body text of the mail.
   */
  public get bodyText(): string {
    return this._bodyText;
  }

  /**
   * Gets the status of the mail.
   * @returns The status of the mail.
   */
  public get status(): EmailStatus {
    return this._status;
  }

  /**
   * Gets the attachments of the mail.
   * @returns The attachments of the mail.
   */
  public get attachments(): Attachment[] {
    return this._attachments;
  }

  /**
   * Gets whether the mail is read.
   * @returns True if the mail is read, false otherwise.
   */
  public get isRead(): boolean {
    return this._isRead;
  }

  /**
   * Gets whether the mail is marked.
   * @returns True if the mail is marked, false otherwise.
   */
  public get isMarked(): boolean {
    return this._isMarked;
  }

  /**
   * Gets the sent date of the mail.
   * @returns The sent date of the mail.
   */
  public get sentAt(): Date | null {
    return this._sentAt;
  }

  /**
   * Gets the received date of the mail.
   * @returns The received date of the mail.
   */
  public get receivedAt(): Date | null {
    return this._receivedAt;
  }

  /**
   * Gets the creation date of the mail.
   * @returns The creation date of the mail.
   */
  public get createdAt(): Date {
    return this._createdAt;
  }

  /**
   * Gets the update date of the mail.
   * @returns The update date of the mail.
   */
  public get updatedAt(): Date {
    return this._updatedAt;
  }

  /**
   * Gets the display date of the mail.
   * @returns The display date of the mail.
   */
  public get displayDate(): string {
    const targetDate = this._receivedAt || this._sentAt || this._createdAt;
    return formatDateDisplay(targetDate);
  }

  /**
   * Gets a snippet of the body text of the mail.
   * @returns A snippet of the body text of the mail.
   */
  public get bodySnippet(): string {
    return truncateText(this._bodyText, 90);
  }

  /**
   * Gets whether the mail has attachments.
   * @returns True if the mail has attachments, false otherwise.
   */
  public get hasAttachments(): boolean {
    return this._attachments.length > 0;
  }
}
