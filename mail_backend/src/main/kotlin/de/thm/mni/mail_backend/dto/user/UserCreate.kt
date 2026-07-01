package de.thm.mni.mail_backend.dto.user

import de.thm.mni.mail_backend.data.Address
import de.thm.mni.mail_backend.data.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import java.time.LocalDateTime
import java.util.UUID

data class UserCreate(
    @field:NotBlank(message = "Password cannot be blank")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*#?&])[A-Za-z\\d@\$!%*#?&]{8,}\$", message = "Password >= 6 chars, upper + lowercase, min 1 number\n and special character")
    var password: String,
    @field:NotBlank(message = "Email cannot be blank")
    @field:Email(message = "Email needs to be valid")
    var email: String,
    @field:NotBlank(message = "Name cannot be blank")
    var displayName: String,
){
    fun toUser(userId: UUID = UUID.randomUUID()): User {
        return User(
            userId = userId,
            password = password,
            address = Address(
                addressId = UUID.randomUUID(),
                email = email,
                displayName = displayName,
            ),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
        )
    }
}
