package de.thm.mni.mail_backend.dto.mail

import de.thm.mni.mail_backend.data.enums.EMailStatus
import de.thm.mni.mail_backend.data.enums.Source
import de.thm.mni.mail_backend.dto.attachments.AttachmentResponse
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.UUID

@Schema(description = "Full email detail response payload.")
data class MailResponse(
    @field:Schema(description = "Unique UUID of the email", example = "987e6543-e21b-12d3-a456-426614174000")
    var mailId: UUID,

    @field:Schema(description = "Sender email address", example = "john.doe@example.com")
    var from: String,

    @field:Schema(description = "List of recipient email addresses", example = "[\"jane.doe@example.com\"]")
    var to: List<String>,

    @field:Schema(description = "List of CC recipient email addresses", example = "[]")
    var cc: List<String> = emptyList(),

    @field:Schema(description = "List of BCC recipient email addresses", example = "[]")
    var bcc: List<String> = emptyList(),

    @field:Schema(description = "List of reply-to email addresses", example = "[]")
    var replyTo: List<String> = emptyList(),

    @field:Schema(description = "Subject of the email", example = "Project Status Update")
    var subject: String,

    @field:Schema(description = "Body text of the email", example = "Hello team...")
    var bodyText: String,

    @field:Schema(description = "Email folder status (INBOX, SENT, DRAFT, TRASH)", example = "INBOX")
    var status: EMailStatus,

    @field:Schema(description = "List of attached files")
    var attachments: List<AttachmentResponse>,

    @field:Schema(description = "Flag indicating whether email has been read", example = "true")
    var isRead: Boolean,

    @field:Schema(description = "Flag indicating whether email is starred/marked", example = "false")
    var isMarked: Boolean,

    @field:Schema(description = "Timestamp when the email was sent", nullable = true)
    var sentAt: LocalDateTime? = null,

    @field:Schema(description = "Timestamp when the email was received", nullable = true)
    var receivedAt: LocalDateTime? = null,

    @field:Schema(description = "Timestamp when the email was created")
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @field:Schema(description = "Timestamp when the email was last updated")
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @field:Schema(description = "Email origin source (e.g. INTERN, EXTERN)", example = "INTERN")
    var source: Source,
)
