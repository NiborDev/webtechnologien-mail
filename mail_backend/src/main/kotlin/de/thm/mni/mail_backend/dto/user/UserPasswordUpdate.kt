package de.thm.mni.mail_backend.dto.user

import de.thm.mni.mail_backend.data.User
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.time.LocalDateTime

/**
 * Data Transfer Object for changing a user's password
 * 
 * @param oldPassword The old password of the user
 * @param newPassword The new password of the user
 * @return The updated user
 */
@Schema(description = "Request payload for changing a users password.")
data class UserPasswordUpdate(
    @field:Schema(description = "Current password for verification", example = "OldSecret123!")
    @field:NotBlank(message = "Old password cannot be empty")
    val oldPassword: String,

    @field:Schema(description = "New password must meet security requirements", example = "NewPasswort1!")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
        message = "Password must be at least 8 characters long and contain at least one letter, one digit and one special character."
    )
    val newPassword: String,
) {
    fun toUser(user: User): User {
        return User(
            userId = user.userId,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            password = newPassword,
            createdAt = user.createdAt,
            updatedAt = LocalDateTime.now(),
        )
    }
}
