package de.thm.mni.mail_backend.dto.attachments

import de.thm.mni.mail_backend.data.Attachments
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import org.springframework.util.MimeType
import java.time.LocalDateTime
import java.util.UUID

data class AttachmentCreate(
    @field:NotBlank(message = "File name cannot be blank")
    var fileName: String,
    @field:Positive(message = "File size must be greater than zero")
    var sizeBytes: Long,
    @field:NotBlank(message = "Storage URL cannot be blank")
    var storageUrl: String,
    @field:NotBlank(message = "MimeType cannot be blank")
    var mimeType: MimeType
) {
    fun toAttachment(attachmentId: UUID = UUID.randomUUID()): Attachments {
        return Attachments(
            attachmentId = attachmentId,
            fileName = fileName,
            sizeBytes = sizeBytes,
            storageUrl = storageUrl,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            mimeType = mimeType
        )
    }
}
