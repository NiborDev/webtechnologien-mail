package de.thm.mni.mail_backend.dto.user

import de.thm.mni.mail_backend.data.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.time.LocalDateTime

/**
 * Data Transfer Object for updating a user
 * 
 * @param email The email of the user
 * @param firstName The first name of the user
 * @param lastName The last name of the user
 */
data class UserUpdate(
    @field:Email(message = "Email needs to be valid")
    val email: String?,
    val firstName: String?,
    val lastName: String?,
) {
    fun toUser(user: User): User {
        return user.copy(
            email = email ?: user.email,
            firstName = firstName ?: user.firstName,
            lastName = lastName ?: user.lastName,
            updatedAt = LocalDateTime.now(),
        )
    }
}
