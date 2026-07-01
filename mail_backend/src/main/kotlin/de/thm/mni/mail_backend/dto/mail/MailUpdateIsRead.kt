package de.thm.mni.mail_backend.dto.mail

import jakarta.validation.constraints.NotNull

data class MailUpdateIsRead(
    @field:NotNull(message = "isRead cannot be null")
    var isRead: Boolean?,
)
