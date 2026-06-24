package de.thm.mni.mail_backend.dto.mail

data class MailListResponse(
    var data: List<MailResponse>,
    var page: Int,
    var size: Int,
    var totalElements: Int,
    var totalPages: Int,
)
