package de.thm.mni.mail_backend.dto.mail

import de.thm.mni.mail_backend.data.Attachments
import de.thm.mni.mail_backend.data.Mail
import de.thm.mni.mail_backend.data.enums.EMailStatus
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime
import java.util.UUID

data class MailCreate(
    @field:Valid
    @field:NotNull(message = "From address cannot be null")
    var from: String,
    @field:Valid
    @field:NotEmpty(message = "To address list cannot be empty")
    var to: List<String>,
    @field:Valid
    var cc: List<String> = emptyList(),
    @field:Valid
    var bcc: List<String> = emptyList(),
    @field:Valid
    var replyTo: List<String> = emptyList(),
    @field:NotBlank(message = "Subject cannot be blank")
    var subject: String,
    @field:NotBlank(message = "Email body cannot be blank")
    var bodyText: String,
    @field:NotNull(message = "Status cannot be null")
    var status: EMailStatus,
    @field:Valid
    var attachments: List<Attachments>,
    @field:Valid
    var source: String,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    var sentAt: LocalDateTime? = null
){
    fun toMail(mailId: UUID = UUID.randomUUID()): Mail {
        return Mail(
            mailId = mailId,
            from = from,
            to = to.toMutableList(),
            cc = cc.toMutableList(),
            bcc = bcc.toMutableList(),
            replyTo = replyTo.toMutableList(),
            subject = subject,
            bodyText = bodyText,
            status = status,
            isRead = false,
            isMarked = false,
            sentAt = null,
            receivedAt = null,
            attachments = attachments.toMutableList()
        )
    }
}
