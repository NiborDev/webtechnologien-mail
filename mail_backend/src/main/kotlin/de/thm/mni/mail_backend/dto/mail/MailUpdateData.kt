package de.thm.mni.mail_backend.dto.mail

import de.thm.mni.mail_backend.data.Attachment
import de.thm.mni.mail_backend.data.Mail
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

//TOOO was ist mit replyTo

data class MailUpdateData(
    var mailId: UUID,
    @field:Valid
    var from: String,
    @field:Valid
    var to: List<String>?,
    @field:Valid
    var cc: List<String>?,
    @field:Valid
    var bcc: List<String>?,
    var subject: String?,
    var bodyText: String?,
    @field:Valid
    var attachments: List<Attachment>?,
) {
    fun toMail(originalMail: Mail): Mail {
        return Mail(
            mailId = originalMail.mailId,
            from = from,
            to = (to ?: originalMail.to).toMutableList(),
            cc = (cc ?: originalMail.cc).toMutableList(),
            bcc = (bcc ?: originalMail.bcc).toMutableList(),
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
