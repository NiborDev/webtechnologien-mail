package de.thm.mni.mail_backend.dto.user

import de.thm.mni.mail_backend.data.User
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
data class UserPasswordUpdate(
    @field:NotBlank(message = "Old password cannot be empty")
    val oldPassword: String,

    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
        message = """
            Password must be at least 8 characters long and contain:
            - At least one letter (A-Z or a-z)
            - At least one digit (0-9)
            - At least one special character from: ! @ $ % * # ? &
        """
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
