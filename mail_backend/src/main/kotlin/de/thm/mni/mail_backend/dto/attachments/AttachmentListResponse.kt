package de.thm.mni.mail_backend.dto.attachments

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Response container for a list of file attachments.")
data class AttachmentListResponse(
    @field:Schema(description = "List of attachment metadata objects")
    var data: List<AttachmentResponse>,
    @field:Schema(description = "Total number of attachments", example = "3")
    var totalElements: Int,
)
