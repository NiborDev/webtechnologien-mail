package de.thm.mni.mail_backend.dto.user

/**
 * Data DTO for listing users for recipient search/selection without exposing internal database IDs.
 */
data class UserListResponse(
    var email: String,
    var firstName: String,
    var lastName: String
)
