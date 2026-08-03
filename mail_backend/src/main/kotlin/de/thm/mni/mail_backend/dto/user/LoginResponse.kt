package de.thm.mni.mail_backend.dto.user

data class LoginResponse(
    val token: String,
    val user: UserResponse
)
