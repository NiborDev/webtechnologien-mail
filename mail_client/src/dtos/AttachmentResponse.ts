/**
 * DTO for an attachment response.
 *
 * @property attachmentId - ID of the attachment
 * @property fileName - name of the file
 * @property sizeBytes - size of the file in bytes
 * @property mimeType - mime type of the file
 */
export type AttachmentResponse = {
  attachmentId: string,
  fileName: string,
  sizeBytes: number,
  mimeType: string
}
