package de.thm.mni.mail_backend.dto.user

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

/**
 * DTO for a user response.
 * 
 * Used to return a user object with all details.
 */
@Schema(description = "This is the information about the user's profile that is sent back.")
data class UserResponse(
    @field:Schema(description = "Unique user UUID identifier", example = "123e4567-e89b-12d3-a456-422114174000")
    var userId: UUID,
    @field:Schema(description = "User email address", example = "john.doe@example.com")
    var email: String,
    @field:Schema(description = "User first name", example = "John")
    var firstName: String,
    @field:Schema(description = "User last name", example = "Doe")
    var lastName: String,
)
