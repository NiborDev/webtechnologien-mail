package de.thm.mni.mail_backend.data

import de.thm.mni.mail_backend.dto.user.UserResponse
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "users")
data class User(
    @Id
    var userId: UUID? = null,

    var password: String = "",

    @Embedded
    var address: Address = Address(),

    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    fun toResponse(): UserResponse {
        return UserResponse(
            userId = requireNotNull(userId) { "userId must not be null" },
            address = address,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }
}
