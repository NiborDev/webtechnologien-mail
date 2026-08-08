package de.thm.mni.mail_backend.data

import de.thm.mni.mail_backend.dto.user.UserListResponse
import de.thm.mni.mail_backend.dto.user.UserResponse
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "users")
class User(
    @Id
    var userId: UUID? = null,

    var password: String = "",
        
    var email: String = "",
        
    var firstName: String = "",
    var lastName: String = "",

    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    /**
     * Converts the User to a UserResponse.
     * 
     * @return The UserResponse.
     */
    fun toResponse(): UserResponse {
        return UserResponse(
            userId = requireNotNull(userId) { "userId must not be null" },
            email = email,
            firstName = firstName,
            lastName = lastName,
        )
    }

    /**
     * Converts the User to a UserListResponse.
     * 
     * @return The UserListResponse.
     */
    fun toUserListResponse(): UserListResponse {
        return UserListResponse(
            email = email,
            firstName = firstName,
            lastName = lastName,
        )
    }
}
