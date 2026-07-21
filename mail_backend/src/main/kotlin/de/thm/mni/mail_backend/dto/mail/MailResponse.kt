package de.thm.mni.mail_backend.dto.mail

import de.thm.mni.mail_backend.data.Attachments
import de.thm.mni.mail_backend.data.enums.EMailStatus
import java.time.LocalDateTime
import java.util.UUID

data class MailResponse(
    var mailId: UUID,
    var from: String,
    var to: List<String>,
    var cc: List<String> = emptyList(),
    var bcc: List<String> = emptyList(),
    var replyTo: List<String> = emptyList(),
    var subject: String,
    var bodyText: String,
    var status: EMailStatus,
    var attachments: List<Attachments>,
    var isRead: Boolean,
    var isMarked: Boolean,
    var sentAt: LocalDateTime? = null,
    var receivedAt: LocalDateTime? = null,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    var source: String,
)
