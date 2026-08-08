package de.thm.mni.mail_backend.dto.attachments

import de.thm.mni.mail_backend.data.Attachment
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import org.springframework.util.MimeType
import org.springframework.util.MimeTypeUtils
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.UUID

/**
 * DTO for creating and initializing attachments from uploaded files.
 * Handles conversion between MultipartFile and Attachment entity.
 *
 * @param fileName Name of the file
 * @param sizeBytes Size of the file in bytes
 * @param storageUrl Path where the file is stored
 * @param mimeType MIME type of the file
 */
@Schema(description = "DTO payload for creating and initializing file attachments.")
data class AttachmentCreate(
    @field:Schema(description = "Original filename", example = "document.pdf")
    @field:NotBlank(message = "File name cannot be blank")
    var fileName: String,

    @field:Schema(description = "File size in bytes", example = "1048576")
    @field:Positive(message = "File size must be greater than zero")
    var sizeBytes: Long,

    @field:Schema(description = "Storage path/URL", example = "uploads/attachments/123e4567-e89b-12d3-a456-426614174000/550e8400-e29b-41d4-a716-446655440000_document.pdf")
    @field:NotBlank(message = "Storage URL cannot be blank")
    var storageUrl: String,

    @field:Schema(description = "MIME type", type = "string", example = "application/pdf")
    @field:NotBlank(message = "MimeType cannot be blank")
    var mimeType: MimeType
) {
    /**
     * Converts this DTO to an Attachment entity.
     *
     * @param attachmentId Optional UUID for the attachment (generates new one if not provided)
     * @return Attachment entity with current timestamp
     */
    fun toAttachment(attachmentId: UUID = UUID.randomUUID()): Attachment {
        return Attachment(
            attachmentId = attachmentId,
            fileName = fileName,
            sizeBytes = sizeBytes,
            storageUrl = storageUrl,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            mimeType = mimeType
        )
    }

    companion object {
        /**
         * Factory method to create AttachmentCreate from a MultipartFile.
         * Automatically extracts file metadata (name, size, MIME type).
         *
         * @param file The uploaded file
         * @param storageUrl The path where the file will be stored
         * @return AttachmentCreate DTO ready for entity conversion
         */
        fun fromFile(file: MultipartFile, storageUrl: String): AttachmentCreate {
            val fileName = file.originalFilename ?: "unknown_file_name"
            val mimeType = file.contentType
                ?.takeIf { it.isNotBlank() }
                ?.let { runCatching { MimeType.valueOf(it) }.getOrNull() }
                ?: MimeTypeUtils.APPLICATION_OCTET_STREAM
            val sizeBytes = file.size

            return AttachmentCreate(
                fileName = fileName,
                sizeBytes = sizeBytes,
                storageUrl = storageUrl,
                mimeType = mimeType
            )
        }
    }
}
