package de.thm.mni.mail_backend.dto.attachments

import org.springframework.util.MimeType
import java.util.UUID

/**
 * Response DTO for attachment metadata.
 * Contains information about an uploaded or stored file attachment.
 *
 * @param attachmentId Unique identifier for the attachment
 * @param fileName Original name of the file
 * @param sizeBytes Size of the file in bytes
 * @param mimeType MIME type of the file (e.g., application/pdf, image/png)
 */
data class AttachmentResponse(
    var attachmentId: UUID,
    var fileName: String,
    var sizeBytes: Long,
    var mimeType: MimeType
)
