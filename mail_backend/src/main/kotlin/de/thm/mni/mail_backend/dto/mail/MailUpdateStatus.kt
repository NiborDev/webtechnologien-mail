package de.thm.mni.mail_backend.dto.mail

import de.thm.mni.mail_backend.data.enums.EMailStatus
import jakarta.validation.constraints.NotNull

data class MailUpdateStatus(
    @field:NotNull(message = "Status cannot be null")
    var status: EMailStatus?,
)
