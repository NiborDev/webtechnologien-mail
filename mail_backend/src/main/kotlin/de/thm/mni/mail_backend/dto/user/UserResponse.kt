package de.thm.mni.mail_backend.dto.user

import java.util.UUID

data class UserResponse(
    var userId: UUID,
    var email: String,
    var firstName: String,
    var lastName: String,
)
