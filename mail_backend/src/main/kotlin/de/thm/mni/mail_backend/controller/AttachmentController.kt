package de.thm.mni.mail_backend.controller

import de.thm.mni.mail_backend.annotations.DefaultApiErrors
import de.thm.mni.mail_backend.dto.attachments.AttachmentResponse
import de.thm.mni.mail_backend.services.AttachmentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
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
@Tag(
    name = "Attachment",
    description = "Operations for uploading, downloading, and deleting file attachments"
)
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
    @Operation(
        operationId = "uploadFiles",
        summary = "Upload file attachments",
        description = "Uploads one or more files as multipart form data and returns their stored metadata."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Files uploaded successfully.",
                content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = ArraySchema(schema = Schema(implementation = AttachmentResponse::class)))]
            ),
            ApiResponse(responseCode = "400", description = "No files provided or invalid file data.")
        ]
    )
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun uploadFiles(
        @Parameter(description = "List of files to upload") @RequestParam("files") files: List<MultipartFile>,
        @AuthenticationPrincipal currentUser: UserDetails
    ): List<AttachmentResponse> {
        logger.info("Received upload request for {} file(s) from user {}", files.size, currentUser.username)
        
        if (files.isEmpty()) {
            logger.warn("Upload request received with no files")
            throw IllegalArgumentException("Upload request must contain at least one file.")
        }
        
        val result = attachmentService.saveAttachments(files, UUID.fromString(currentUser.username))
        logger.info("Upload successful: {} file(s) saved for user {}", result.size, currentUser.username)
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
    @Operation(
        operationId = "downloadFile",
        summary = "Download attachment by ID",
        description = "Downloads the file attachment by its UUID if the user has access to the containing email."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "File successfully downloaded.",
                content = [Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)]
            ),
            ApiResponse(responseCode = "404", description = "Attachment not found or user does not have access.")
        ]
    )
    @GetMapping("/{id}")
    fun downloadFile(
        @Parameter(description = "UUID of the attachment") @PathVariable id: UUID,
        @AuthenticationPrincipal currentUser: UserDetails
    ): ResponseEntity<Resource> {
        val (attachment, resource) = attachmentService.loadFileAsResource(id, UUID.fromString(currentUser.username))
        val response = attachment.toResponse()
        
        logger.debug("File retrieved successfully: {}", response.fileName)
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(response.mimeType.toString()))
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"${response.fileName}\""
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
    @Operation(
        operationId = "deleteAttachment",
        summary = "Delete attachment",
        description = "Deletes an attachment from a draft email if the user is the owner."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Attachment deleted.")
        ]
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAttachment(
        @Parameter(description = "UUID of the attachment to delete") @PathVariable id: UUID,
        @AuthenticationPrincipal currentUser: UserDetails
    ) {
        logger.info("Delete request for attachment {} by user {}", id, currentUser.username)
        attachmentService.deleteAttachment(id, UUID.fromString(currentUser.username))
        logger.info("Attachment {} deleted successfully by user {}", id, currentUser.username)
    }
}
