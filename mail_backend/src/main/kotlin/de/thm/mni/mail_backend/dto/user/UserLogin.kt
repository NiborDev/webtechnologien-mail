package de.thm.mni.mail_backend.dto.user

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

/**
 * DTO for user login.
 * 
 * Used to send a request to log in.
 */
@Schema(description = "The data of the user that is used to log in.")
data class UserLogin(
    @field:Schema(description = "User email address", example = "john.doe@example.com")
    @field:NotBlank(message = "Email cannot be blank")
    @field:Email(message = "Email needs to be valid")
    var email: String,

    @field:Schema(description = "User password", example = "Secret123!")
    @field:NotBlank(message = "Password cannot be blank")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
        message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number and one special character"
    )
    var password: String
)
