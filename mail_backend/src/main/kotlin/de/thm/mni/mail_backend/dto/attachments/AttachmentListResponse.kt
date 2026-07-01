package de.thm.mni.mail_backend.dto.attachments

data class AttachmentListResponse(
    var data: List<AttachmentResponse>,
    var totalElements: Int,
)
