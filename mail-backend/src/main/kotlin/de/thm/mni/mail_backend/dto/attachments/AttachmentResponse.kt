package de.thm.mni.mail_backend.dto.attachments

import java.time.LocalDateTime
import java.util.UUID

data class AttachmentResponse(
    var attachmentId: UUID,
    var fileName: String,
    var sizeBytes: Long,
    var storageUrl: String,
    var createdAt: LocalDateTime,
)
