package de.thm.mni.mail_backend.dto.user

import de.thm.mni.mail_backend.data.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.time.LocalDateTime

//TODO: Passwörter encoden/korrekt speichern

data class UserUpdate(
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}\$", message = "Password >= 6 chars, upper + lowercase, min 1 number\n and special character")
    var newPassword: String?,
    var oldPassword: String?,
    @field:Email(message = "Email needs to be valid")
    var email: String?,
    var firstName: String?,
    var lastName: String?,
) {
    fun toUser(originalUser: User): User {
        return originalUser.copy(
            password = newPassword ?: originalUser.password,
            email = email ?: originalUser.email,
            firstName = firstName ?: originalUser.firstName,
            lastName = lastName ?: originalUser.lastName,
            updatedAt = LocalDateTime.now(),
        )
    }
}
