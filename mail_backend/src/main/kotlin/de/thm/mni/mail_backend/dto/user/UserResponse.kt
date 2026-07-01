package de.thm.mni.mail_backend.dto.user

import de.thm.mni.mail_backend.data.Address
import java.time.LocalDateTime
import java.util.UUID

data class UserResponse(
    var userId: UUID,
    var address: Address,
    var createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
)
