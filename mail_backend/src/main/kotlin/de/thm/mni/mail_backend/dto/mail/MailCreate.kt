package de.thm.mni.mail_backend.dto.mail

import de.thm.mni.mail_backend.data.Address
import de.thm.mni.mail_backend.data.Attachments
import de.thm.mni.mail_backend.data.Mail
import de.thm.mni.mail_backend.data.enums.EMailStatus
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class MailCreate(
    @field:Valid
    @field:NotNull(message = "From address cannot be null")
    var from: Address,
    @field:Valid
    @field:NotEmpty(message = "To address list cannot be empty")
    var to: List<Address>,
    @field:Valid
    var cc: List<Address> = emptyList(),
    @field:Valid
    var bcc: List<Address> = emptyList(),
    @field:Valid
    var replyTo: Address?,
    @field:NotBlank(message = "Subject cannot be blank")
    var subject: String,
    @field:NotBlank(message = "Email body cannot be blank")
    var bodyText: String,
    @field:NotNull(message = "Status cannot be null")
    var status: EMailStatus,
    @field:Valid
    var attachments: List<Attachments>
){
    fun toMail(mailId: UUID = UUID.randomUUID()): Mail {
        return Mail(
            mailId = mailId,
            from = from,
            to = to,
            cc = cc,
            bcc = bcc,
            replyTo = replyTo,
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
