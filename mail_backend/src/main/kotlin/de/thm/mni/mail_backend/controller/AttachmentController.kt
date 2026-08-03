package de.thm.mni.mail_backend.controller

import de.thm.mni.mail_backend.annotations.DefaultApiErrors
import de.thm.mni.mail_backend.dto.attachments.AttachmentResponse
import de.thm.mni.mail_backend.services.AttachmentService
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

/**
 * Controller for managing file attachments.
 * Provides endpoints for uploading, downloading, and deleting attachments.
 */
@DefaultApiErrors
@RestController
@RequestMapping("/attachments")
class AttachmentController(
    private val attachmentService: AttachmentService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * POST /attachments
     * Upload the provided attachments and returns JSON metadata.
     * 
     * @param files The files to upload
     * @return 201 Created with attachment metadata
     */
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun uploadFiles(
        @RequestParam("files") files: List<MultipartFile>
    ): List<AttachmentResponse> {
        logger.info("Received upload request for {} file(s)", files.size)
        
        if (files.isEmpty()) {
            logger.warn("Upload request received with no files")
            throw IllegalArgumentException("Upload request must contain at least one file.")
        }
        
        val result = attachmentService.saveAttachments(files)
        logger.info("Upload successful: {} file(s) saved", result.size)
        return result
    }

    /**
     * GET /attachments/{id}
     * Loads the file for download.
     * Only users who have access to the email containing this attachment can download it.
     * 
     * @param id The attachment ID
     * @param currentUser The authenticated user
     * @return 200 OK with file content, or 404 Not Found if attachment doesn't exist or user has no access
     */
    @GetMapping("/{id}")
    fun downloadFile(
        @PathVariable id: UUID,
        @AuthenticationPrincipal currentUser: UserDetails
    ): ResponseEntity<Resource> {
        logger.info("Download request for attachment {} by user {}", id, currentUser.username)
        val (attachment, resource) = attachmentService.loadFileAsResource(id, UUID.fromString(currentUser.username))
        
        logger.debug("File retrieved successfully: {}", attachment.fileName)
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(attachment.mimeType.toString()))
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"${attachment.fileName}\""
            ).body(resource)
    }

    /**
     * DELETE /attachments/{id}
     * Deletes an attachment if the current user is the owner (author) and the email has not yet been sent.
     * Returns 204 No Content for all deletion requests to ensure status code idempotency and security.
     * 
     * @param id The attachment ID
     * @param currentUser The authenticated user
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAttachment(
        @PathVariable id: UUID,
        @AuthenticationPrincipal currentUser: UserDetails
    ) {
        logger.info("Delete request for attachment {} by user {}", id, currentUser.username)

        try {
            attachmentService.deleteAttachment(id, UUID.fromString(currentUser.username))
            logger.info("Attachment {} deleted successfully by user {}", id, currentUser.username)
        } catch (e: Exception) {
            logger.warn("Idempotent attachment deletion handling for attachment {}: {}", id, e.message)
        }
    }
}
