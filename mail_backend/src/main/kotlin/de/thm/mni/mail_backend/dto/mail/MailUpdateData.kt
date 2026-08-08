package de.thm.mni.mail_backend.dto.mail

import de.thm.mni.mail_backend.data.Attachment
import de.thm.mni.mail_backend.data.Mail
import de.thm.mni.mail_backend.dto.attachments.AttachmentResponse
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import java.util.UUID

/**
 * DTO for updating a mail.
 * 
 * Used to send a request to update a mail.
 */
@Schema(description = "Request payload for updating editable content of a draft email.")
data class MailUpdateData(
    @field:Schema(description = "UUID of the email to update", example = "987e6543-e21b-12d3-a456-426614174000")
    var mailId: UUID,

    @field:Schema(description = "Updated recipient email list", nullable = true)
    @field:Valid
    var to: List<String>?,

    @field:Schema(description = "Updated CC recipient email list", nullable = true)
    @field:Valid
    var cc: List<String>?,

    @field:Schema(description = "Updated BCC recipient email list", nullable = true)
    @field:Valid
    var bcc: List<String>?,

    @field:Schema(description = "Updated subject line", example = "Updated Subject", nullable = true)
    var subject: String?,

    @field:Schema(description = "Updated reply-to list", nullable = true)
    var replyTo: List<String>?,

    @field:Schema(description = "Updated email body text", example = "Updated text body...", nullable = true)
    var bodyText: String?,

    @field:Schema(description = "Updated list of file attachments", nullable = true)
    @field:Valid
    var attachments: List<AttachmentResponse>?,
) {
    fun toMail(originalMail: Mail): Mail {
        return Mail(
            mailId = originalMail.mailId,
            from = originalMail.from,
            to = (to ?: originalMail.to).toMutableList(),
            cc = (cc ?: originalMail.cc).toMutableList(),
            bcc = (bcc ?: originalMail.bcc).toMutableList(),
            replyTo = (replyTo?: originalMail.replyTo).toMutableList(),
            subject = subject ?: originalMail.subject,
            bodyText = bodyText ?: originalMail.bodyText,
            status = originalMail.status,
            isRead = originalMail.isRead,
            isMarked = originalMail.isMarked,
            sentAt = originalMail.sentAt,
            receivedAt = originalMail.receivedAt,
            attachments = attachments?.map { att ->
                Attachment(
                    attachmentId = att.attachmentId,
                    fileName = att.fileName,
                    sizeBytes = att.sizeBytes,
                    mimeType = att.mimeType
                )
            }?.toMutableList() ?: originalMail.attachments,
        )
    }
}
