package de.thm.mni.mail_backend.dto.mail

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "Request payload for updating the read status of an email.")
data class MailUpdateIsRead(
    @field:Schema(description = "Whether the email is read (true) or unread (false)", example = "true")
    @field:NotNull(message = "isRead cannot be null")
    var isRead: Boolean?,
)
