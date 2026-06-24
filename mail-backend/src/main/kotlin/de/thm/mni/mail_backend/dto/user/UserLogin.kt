package de.thm.mni.mail_backend.dto.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class UserLogin(
    @field:NotBlank(message = "Email cannot be blank")
    @field:Email(message = "Email needs to be valid")
    var email: String,
    @field:NotBlank(message = "Password cannot be blank")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}\$", message = "Password >= 6 chars, upper + lowercase, min 1 number\n and special character")
    var password: String
)
