package de.thm.mni.mail_backend.dto.attachments

import de.thm.mni.mail_backend.data.Attachments
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import org.springframework.util.MimeType
import java.time.LocalDateTime
import java.util.UUID

data class AttachmentUpdate(
    var attachmentId: UUID,
    @field:NotBlank(message = "File name cannot be blank")
    var fileName: String?,
    @field:Positive(message = "File size must be greater than zero")
    var sizeBytes: Long?,
    @field:NotBlank(message = "Storage URL cannot be blank")
    var storageUrl: String?,
    var mimeType: MimeType?
) {
    fun toAttachment(originalAttachment: Attachments): Attachments {
        return originalAttachment.copy(
            fileName = fileName ?: originalAttachment.fileName,
            sizeBytes = sizeBytes ?: originalAttachment.sizeBytes,
            storageUrl = storageUrl ?: originalAttachment.storageUrl,
            createdAt = originalAttachment.createdAt,
            updatedAt = LocalDateTime.now(),
            mimeType = mimeType ?: originalAttachment.mimeType
        )
    }
}

