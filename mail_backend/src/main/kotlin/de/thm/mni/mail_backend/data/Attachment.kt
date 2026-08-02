package de.thm.mni.mail_backend.data

import de.thm.mni.mail_backend.dto.attachments.AttachmentResponse
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.util.MimeType
import java.time.LocalDateTime
import java.util.UUID

/**
 * Entity representing a file attachment in the mail system.
 * Files are stored on the filesystem, with metadata persisted in the database.
 *
 * @param attachmentId Unique identifier for the attachment
 * @param fileName Original name of the uploaded file
 * @param sizeBytes Size of the file in bytes
 * @param storageUrl Full path to the file on the filesystem
 * @param createdAt Timestamp when the attachment was uploaded
 * @param updatedAt Timestamp of last modification
 * @param mimeType MIME type of the file (e.g., application/pdf, image/png)
 */
@Entity
@Table(name = "attachments")
class Attachment(
    @Id
    var attachmentId: UUID? = null,
    var fileName: String = "",
    var sizeBytes: Long = 0L,
    var storageUrl: String = "",
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    var mimeType: MimeType = MimeType("text/plain"),
) {
    /**
     * Converts this entity to an AttachmentResponse DTO.
     * Suitable for REST API responses.
     *
     * @return AttachmentResponse with all public attachment information
     * @throws IllegalArgumentException if attachmentId is null
     */
    fun toResponse(): AttachmentResponse {
        return AttachmentResponse(
            attachmentId = requireNotNull(attachmentId) { "attachmentId must not be null" },
            fileName = fileName,
            sizeBytes = sizeBytes,
            mimeType = mimeType
        )
    }
}
