package de.thm.mni.mail_backend.dto.attachments

import org.springframework.util.MimeType
import java.time.LocalDateTime
import java.util.UUID

data class AttachmentResponse(
    var attachmentId: UUID,
    var fileName: String,
    var sizeBytes: Long,
    var storageUrl: String,
    var createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
    var mimeType: MimeType
)
