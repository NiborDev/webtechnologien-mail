package de.thm.mni.mail_backend.dto.mail

import de.thm.mni.mail_backend.data.Attachment
import de.thm.mni.mail_backend.data.Mail
import de.thm.mni.mail_backend.data.enums.EMailStatus
import de.thm.mni.mail_backend.data.enums.Source
import de.thm.mni.mail_backend.dto.attachments.AttachmentResponse
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import java.time.LocalDateTime
import java.util.UUID

@Schema(description = "Request payload for creating a new email draft.")
data class MailCreate(
    @field:Schema(description = "List of recipient email addresses", example = "[\"jane.doe@example.com\"]")
    @field:Valid
    var to: List<String>,

    @field:Schema(description = "List of CC recipient email addresses", example = "[\"boss@example.com\"]")
    @field:Valid
    var cc: List<String> = emptyList(),

    @field:Schema(description = "List of BCC recipient email addresses", example = "[]")
    @field:Valid
    var bcc: List<String> = emptyList(),

    @field:Schema(description = "List of reply-to email addresses", example = "[]")
    @field:Valid
    var replyTo: List<String> = emptyList(),

    @field:Schema(description = "Email subject line", example = "Project Status Update")
    var subject: String,

    @field:Schema(description = "Plain text body content of the email", example = "Hello team, here is the weekly report...")
    var bodyText: String,

    @field:Schema(description = "List of associated file attachments")
    @field:Valid
    var attachments: List<AttachmentResponse> = emptyList(),
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
            attachments = attachments.map { att ->
                Attachment(
                    attachmentId = att.attachmentId,
                    fileName = att.fileName,
                    sizeBytes = att.sizeBytes,
                    mimeType = att.mimeType
                )
            }.toMutableList()
        )
    }
}
