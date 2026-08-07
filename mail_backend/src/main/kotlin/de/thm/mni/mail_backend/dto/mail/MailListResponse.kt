package de.thm.mni.mail_backend.dto.mail

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Paginated list response container for emails.")
data class MailListResponse(
    @field:Schema(description = "List of email items in the current page")
    var data: List<MailResponse>,
    @field:Schema(description = "Current page index (0-indexed)", example = "0")
    var page: Int,
    @field:Schema(description = "Page size (number of items per page)", example = "20")
    var size: Int,
    @field:Schema(description = "Total element count across all pages", example = "42")
    var totalElements: Int,
    @field:Schema(description = "Total number of available pages", example = "3")
    var totalPages: Int,
)
