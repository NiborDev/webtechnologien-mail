package de.thm.mni.mail_backend.dto.mail

import de.thm.mni.mail_backend.data.enums.EMailStatus
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

/**
 * DTO for updating the status folder of a mail.
 * 
 * Used to send a request to update the status folder of a mail.
 */
@Schema(description = "Request payload for updating the status folder of an email.")
data class MailUpdateStatus(
    @field:Schema(description = "Target status folder (e.g. Draft, sent, received)", example = "Draft")
    @field:NotNull(message = "Status cannot be null")
    var status: EMailStatus?,
)
