import { Attachment } from './Attachment'
import { EMailStatus } from '../enums/email-status'
import { Source } from '../enums/source'

export class Mail {
  constructor(
    private _mailId: string,
    private _from: string,
    private _to: string[],
    private _cc: string[],
    private _bcc: string[],
    private _replyTo: string[],
    private _subject: string,
    private _bodyText: string,
    private _status: EMailStatus,
    private _attachments: Attachment[],
    private _isRead: boolean,
    private _isMarked: boolean,
    private _sentAt: Date | null,
    private _receivedAt: Date | null,
    private _source: Source,
  ) {}

  public get mailId(): string {
    return this._mailId
  }

  public get from(): string {
    return this._from
  }

  public get to(): string[] {
    return this._to
  }

  public get cc(): string[] {
    return this._cc
  }

  public get bcc(): string[] {
    return this._bcc
  }

  public get replyTo(): string[] {
    return this._replyTo
  }

  public get subject(): string {
    return this._subject
  }

  public get bodyText(): string {
    return this._bodyText
  }

  public get status(): EMailStatus {
    return this._status
  }

  public get attachments(): Attachment[] {
    return this._attachments
  }

  public get isRead(): boolean {
    return this._isRead
  }

  public get isMarked(): boolean {
    return this._isMarked
  }

  public get sentAt(): Date | null {
    return this._sentAt
  }

  public get receivedAt(): Date | null {
    return this._receivedAt
  }

  public get source(): Source {
    return this._source
  }

  public set from(value: string) {
    this._from = value
  }

  public set to(value: string[]) {
    this._to = value
  }

  public set cc(value: string[]) {
    this._cc = value
  }

  public set bcc(value: string[]) {
    this._bcc = value
  }

  public set replyTo(value: string[]) {
    this._replyTo = value
  }

  public set subject(value: string) {
    this._subject = value
  }

  public set bodyText(value: string) {
    this._bodyText = value
  }

  public set status(value: EMailStatus) {
    this._status = value
  }

  public set attachments(value: Attachment[]) {
    this._attachments = value
  }

  public set isRead(value: boolean) {
    this._isRead = value
  }

  public set isMarked(value: boolean) {
    this._isMarked = value
  }

  public set sentAt(value: Date | null) {
    this._sentAt = value
  }

  public set receivedAt(value: Date | null) {
    this._receivedAt = value
  }

  public set source(value: Source) {
    this._source = value
  }
}
