/**
 * Data Transfer Object for an attachment.
 *
 * @property {string} attachmentId - The unique identifier of the attachment.
 * @property {string} filename - The filename of the attachment.
 * @property {string} contentType - The content type of the attachment.
 * @property {number} size - The size of the attachment in bytes.
 */
export interface AttachmentDto {
  attachmentId?: string;
  filename?: string;
  contentType?: string;
  size?: number;
}
