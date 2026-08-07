package de.thm.mni.mail_backend.dto.mail

import de.thm.mni.mail_backend.data.Attachment
import de.thm.mni.mail_backend.data.Mail
import de.thm.mni.mail_backend.data.enums.EMailStatus
import de.thm.mni.mail_backend.data.enums.Source
import jakarta.validation.Valid
import java.time.LocalDateTime
import java.util.UUID

data class MailCreate(
    @field:Valid
    var to: List<String>,
    @field:Valid
    var cc: List<String> = emptyList(),
    @field:Valid
    var bcc: List<String> = emptyList(),
    @field:Valid
    var replyTo: List<String> = emptyList(),
    var subject: String,
    var bodyText: String,
    @field:Valid
    var attachments: List<Attachment>,
){
    fun toMail(mailId: UUID = UUID.randomUUID()): Mail {
        return Mail(
            mailId = mailId,
            from = "",
            to = to.toMutableList(),
            cc = cc.toMutableList(),
            bcc = bcc.toMutableList(),
            replyTo = replyTo.toMutableList(),
            subject = subject,
            bodyText = bodyText,
            status = EMailStatus.DRAFT,
            source = Source.INTERN,
            isRead = false,
            isMarked = false,
            createdAt = LocalDateTime.now(),
            sentAt = null,
            updatedAt = LocalDateTime.now(),
            receivedAt = null,
            attachments = attachments.toMutableList()
        )
    }
}
