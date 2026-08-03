package de.thm.mni.mail_backend.controller

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
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

/**
 * Controller for managing file attachments.
 * Provides endpoints for uploading and downloading attachments.
 */
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
     * @return 201 Created with attachment metadata, or 400 Bad Request if files are invalid
     */
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadFiles(
        @RequestParam("files") files: List<MultipartFile>
    ): ResponseEntity<List<AttachmentResponse>> {
        logger.info("Received upload request for {} file(s)", files.size)
        
        if (files.isEmpty()) {
            logger.warn("Upload request received with no files")
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
        
        return try {
            val result = attachmentService.saveAttachments(files)
            logger.info("Upload successful: {} file(s) saved", result.size)
            ResponseEntity.status(HttpStatus.CREATED).body(result)
        } catch (e: IllegalArgumentException) {
            logger.warn("Upload validation failed: {}", e.message)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        } catch (e: Exception) {
            logger.error("Error during file upload", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    /**
     * GET /attachments/{id}
     * Loads the file for download.
     * Only users who have access to the email containing this attachment can download it.
     * 
     * @param id The attachment ID
     * @param currentUser The authenticated user
     * @return 200 OK with file content, 403 Forbidden if no access, or 404 Not Found if attachment doesn't exist
     */
    @GetMapping("/{id}")
    fun downloadFile(
        @PathVariable id: UUID,
        @AuthenticationPrincipal currentUser: UserDetails
    ): ResponseEntity<Resource> {
        logger.info("Download request for attachment {} by user {}", id, currentUser.username)
        
        return try {
            val (attachment, resource) = attachmentService.loadFileAsResource(id)
            
            logger.debug("File retrieved successfully: {}", attachment.fileName)
            ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.mimeType.toString()))
                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"${attachment.fileName}\""
                ).body(resource)
        } catch (e: AccessDeniedException) {
            logger.warn("Access denied for user {} to attachment {}", currentUser.username, id)
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        } catch (e: Exception) {
            logger.warn("Attachment not found: {}", id)
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}
