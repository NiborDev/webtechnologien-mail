package de.thm.mni.mail_backend.dto.user

import de.thm.mni.mail_backend.data.User
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import java.time.LocalDateTime

/**
 * Data Transfer Object for updating a user
 * 
 * @param email The email of the user
 * @param firstName The first name of the user
 * @param lastName The last name of the user
 */
@Schema(description = "Request payload for updating user profile fields.")
data class UserUpdate(
    @field:Schema(description = "Updated email address", example = "john.doe@example.com", nullable = true)
    @field:Email(message = "Email needs to be valid")
    val email: String?,

    @field:Schema(description = "Updated first name", example = "John", nullable = true)
    val firstName: String?,

    @field:Schema(description = "Updated last name", example = "Doe", nullable = true)
    val lastName: String?,
) {
    /**
     * Converts the UserUpdate to a User entity.
     * 
     * @param user The user to update.
     * @return The updated user.
     */
    fun toUser(user: User): User {
        return User(
            userId = user.userId,
            email = email ?: user.email,
            firstName = firstName ?: user.firstName,
            lastName = lastName ?: user.lastName,
            password = user.password,
            createdAt = user.createdAt,
            updatedAt = LocalDateTime.now(),
        )
    }
}
