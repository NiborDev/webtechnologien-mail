package de.thm.mni.mail_backend.dto.mail

import de.thm.mni.mail_backend.data.Address
import de.thm.mni.mail_backend.data.Attachments
import de.thm.mni.mail_backend.data.enums.EMailStatus
import java.time.LocalDateTime
import java.util.UUID

data class MailResponse(
    var mailId: UUID,
    var from: Address,
    var to: List<Address>,
    var cc: List<Address> = emptyList(),
    var bcc: List<Address> = emptyList(),
    var replyTo: List<Address> = emptyList(),
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
