package de.thm.mni.mail_backend.dto.user

import de.thm.mni.mail_backend.data.User
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.time.LocalDateTime
import java.util.UUID

/**
 * DTO for creating a new user account.
 * 
 * Used to send a request to create a new user account.
 */
@Schema(description = "Request payload for creating a user account.")
data class UserCreate(
    @field:Schema(description = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number and one special character", example = "Secret123!")
    @field:NotBlank(message = "Password cannot be blank")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
        message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number and one special character"
    )
    var password: String,

    @field:Schema(description = "User email address", example = "john.doe@example.com")
    @field:NotBlank(message = "Email cannot be blank")
    @field:Email(message = "Email needs to be valid")
    var email: String,

    @field:Schema(description = "User first name", example = "John")
    @field:NotBlank(message = "First Name cannot be blank")
    var firstName: String,

    @field:Schema(description = "User last name", example = "Doe")
    @field:NotBlank(message = "Last Name cannot be blank")
    var lastName: String,
){
    /**
     * Converts the UserCreate to a User entity.
     * 
     * @param userId The ID of the user, defaults to a random UUID.
     * @return The User entity.
     */
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
