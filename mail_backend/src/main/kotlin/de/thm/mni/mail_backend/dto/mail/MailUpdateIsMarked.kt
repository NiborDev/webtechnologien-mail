package de.thm.mni.mail_backend.dto.mail

import jakarta.validation.constraints.NotNull

data class MailUpdateIsMarked(
    @field:NotNull(message = "isMarked cannot be null")
    var isMarked: Boolean?,
)
