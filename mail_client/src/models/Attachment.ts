import { AttachmentResponse } from '../dtos/AttachmentResponse';

/**
 * Model representing an attachment.
 *
 * @property {string} attachmentId - The unique identifier of the attachment.
 * @property {string} fileName - The filename of the attachment.
 * @property {number} sizeBytes - The size of the attachment in bytes.
 * @property {string} mimeType - The mime type of the attachment.
 */
export class Attachment {
  /**
   * Constructs an Attachment instance.
   *
   * @param _attachmentId - Unique identifier of the attachment.
   * @param _fileName - Filename of the attachment.
   * @param _sizeBytes - Size of the attachment in bytes.
   * @param _mimeType - MIME type of the attachment.
   */
  constructor(
    private _attachmentId: string,
    private _fileName: string,
    private _sizeBytes: number,
    private _mimeType: string,
  ) {}

  /**
   * Factory method to create an Attachment domain model from an AttachmentResponse DTO.
   *
   * @param dto The AttachmentResponse DTO to convert.
   * @returns A new Attachment model instance.
   */
  public static fromDto(dto: AttachmentResponse): Attachment {
    let mimeType = 'application/octet-stream';

    if (typeof dto.mimeType === 'string') {
      mimeType = dto.mimeType;
    } else if (dto.mimeType && typeof dto.mimeType === 'object') {
      const mimeObj = dto.mimeType as { type?: string; subtype?: string };
      mimeType = `${mimeObj.type || 'application'}/${mimeObj.subtype || 'octet-stream'}`;
    }

    return new Attachment(
      dto.attachmentId,
      dto.fileName || '',
      dto.sizeBytes || 0,
      mimeType,
    );
  }

  /**
   * Converts this Attachment model instance to an AttachmentResponse DTO.
   *
   * @returns An AttachmentResponse DTO object.
   */
  public toDto(): AttachmentResponse {
    return {
      attachmentId: this._attachmentId,
      fileName: this._fileName,
      sizeBytes: this._sizeBytes,
      mimeType: this._mimeType,
    };
  }

  /** Gets the attachment ID. */
  public get attachmentId(): string {
    return this._attachmentId;
  }

  /** Gets the file name. */
  public get fileName(): string {
    return this._fileName;
  }

  /** Gets the size in bytes. */
  public get sizeBytes(): number {
    return this._sizeBytes;
  }

  /** Gets the MIME type. */
  public get mimeType(): string {
    return this._mimeType;
  }
}
