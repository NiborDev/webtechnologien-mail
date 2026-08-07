package de.thm.mni.mail_backend.dto.user

import io.swagger.v3.oas.annotations.media.Schema

/**
 * Data DTO for listing users for recipient search/selection without exposing internal database IDs.
 */
@Schema(description = "Summary user information for recipient search and user directory listings.")
data class UserListResponse(
    @field:Schema(description = "User email address", example = "john.doe@example.com")
    var email: String,
    @field:Schema(description = "User first name", example = "John")
    var firstName: String,
    @field:Schema(description = "User last name", example = "Doe")
    var lastName: String
)
