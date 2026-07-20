package de.thm.mni.mail_backend.data

import jakarta.persistence.Embeddable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.util.UUID

//not used anymore

@Embeddable
data class Address(
    var addressId: UUID? = null,

    @field:NotBlank(message = "Email cannot be blank")
    @field:Email(message = "Invalid email format")
    var email: String = "",

    var displayName: String? = null
)
