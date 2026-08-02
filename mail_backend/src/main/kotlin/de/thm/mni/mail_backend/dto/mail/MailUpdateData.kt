package de.thm.mni.mail_backend.dto.mail

import de.thm.mni.mail_backend.data.Attachment
import de.thm.mni.mail_backend.data.Mail
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

data class MailUpdateData(
    var mailId: UUID,
    @field:Valid
    var from: String,
    @field:Valid
    @field:Size(min = 1, message = "To address list cannot be empty")
    var to: List<String>?,
    @field:Valid
    var cc: List<String>?,
    @field:Valid
    var bcc: List<String>?,
    @field:NotBlank(message = "Subject cannot be blank")
    var subject: String?,
    @field:NotBlank(message = "Email body cannot be blank")
    var bodyText: String?,
    @field:Valid
    var attachments: List<Attachment>?,
) {
    fun toMail(originalMail: Mail): Mail {
        return Mail(
            mailId = originalMail.mailId,
            from = from,
            to = to ?: originalMail.to,
            cc = cc ?: originalMail.cc,
            bcc = bcc ?: originalMail.bcc,
            replyTo = originalMail.replyTo,
            subject = subject ?: originalMail.subject,
            bodyText = bodyText ?: originalMail.bodyText,
            status = originalMail.status,
            isRead = originalMail.isRead,
            isMarked = originalMail.isMarked,
            sentAt = originalMail.sentAt,
            receivedAt = originalMail.receivedAt,
            attachments = (attachments ?: originalMail.attachments).toMutableList(),
        )
    }
}
