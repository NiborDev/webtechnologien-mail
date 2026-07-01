package de.thm.mni.mail_backend.dto.user

import de.thm.mni.mail_backend.data.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.time.LocalDateTime

data class UserUpdate(
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}\$", message = "Password >= 6 chars, upper + lowercase, min 1 number\n and special character")
    @field:NotBlank(message = "New password cannot be blank")
    var newPaddword: String?,
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}\$", message = "Password >= 6 chars, upper + lowercase, min 1 number\n and special character")
    @field:NotBlank(message = "Old password cannot be blank")
    var oldPassword: String?,
    @field:Email(message = "Email needs to be valid")
    @field:NotBlank(message = "Email cannot be blank")
    var email: String?,
    @field:NotBlank(message = "Name cannot be blank")
    var displayName: String?,
) {
    fun toUser(originalUser: User): User {
        return originalUser.copy(
            password = newPaddword ?: originalUser.password,
            address = originalUser.address.copy(
                email = email ?: originalUser.address.email,
                displayName = displayName ?: originalUser.address.displayName,
            ),
            updatedAt = LocalDateTime.now(),
        )
    }
}
