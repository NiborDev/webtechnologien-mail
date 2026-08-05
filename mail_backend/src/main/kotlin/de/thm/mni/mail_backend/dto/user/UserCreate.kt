package de.thm.mni.mail_backend.dto.user

import de.thm.mni.mail_backend.data.User
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.time.LocalDateTime
import java.util.UUID

@Schema(description = "Request payload for creating a user account.")
data class UserCreate(
    @field:NotBlank(message = "Password cannot be blank")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "Password >= 6 chars, upper + lowercase, min 1 number\n and special character")
    var password: String,
    @field:NotBlank(message = "Email cannot be blank")
    @field:Email(message = "Email needs to be valid")
    var email: String,
    @field:NotBlank(message = "First Name cannot be blank")
    var firstName: String,
    @field:NotBlank(message = "Last Name cannot be blank")
    var lastName: String,
){
    fun toUser(userId: UUID = UUID.randomUUID()): User {
        return User(
            userId = userId,
            password = password,
            email = email,
            firstName = firstName,
            lastName = lastName,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
        )
    }
}
