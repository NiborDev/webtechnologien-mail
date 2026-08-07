package de.thm.mni.mail_backend.dto.user

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Response payload returned upon successful login authentication.")
data class LoginResponse(
    @field:Schema(description = "JWT Bearer Authentication Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val token: String,
    
    @field:Schema(description = "Authenticated user profile information")
    val user: UserResponse
)
