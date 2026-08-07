package de.thm.mni.mail_backend.dto.attachments

import io.swagger.v3.oas.annotations.media.Schema
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
@Schema(description = "Response metadata for an uploaded or stored file attachment.")
data class AttachmentResponse(
    @field:Schema(description = "Unique UUID identifier for the attachment", example = "a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d")
    var attachmentId: UUID,
    @field:Schema(description = "Original filename with extension", example = "document.pdf")
    var fileName: String,
    @field:Schema(description = "File size in bytes", example = "1048576")
    var sizeBytes: Long,
    @field:Schema(description = "MIME content type of the file", type = "string", example = "application/pdf")
    var mimeType: MimeType
)
