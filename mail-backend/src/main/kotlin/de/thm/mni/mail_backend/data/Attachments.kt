package de.thm.mni.mail_backend.data

import de.thm.mni.mail_backend.dto.attachments.AttachmentResponse
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "attachments")
data class Attachments(
    @Id
    var attachmentId: UUID? = null,
    var fileName: String = "",
    var sizeBytes: Long = 0,
    var storageUrl: String = "",
    var createdAt: LocalDateTime = LocalDateTime.now()
) {
    fun toResponse(): AttachmentResponse {
        return AttachmentResponse(
            attachmentId = requireNotNull(attachmentId) { "attachmentId must not be null" },
            fileName = fileName,
            sizeBytes = sizeBytes,
            storageUrl = storageUrl,
            createdAt = createdAt
        )
    }
}
