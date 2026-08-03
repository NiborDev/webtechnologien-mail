package de.thm.mni.mail_backend.services

import de.thm.mni.mail_backend.data.Attachment
import de.thm.mni.mail_backend.dto.attachments.AttachmentCreate
import de.thm.mni.mail_backend.dto.attachments.AttachmentResponse
import de.thm.mni.mail_backend.exceptions.StorageFileNotFoundException
import de.thm.mni.mail_backend.repositories.interfaces.AttachmentRepository
import de.thm.mni.mail_backend.repositories.interfaces.MailRepository
import de.thm.mni.mail_backend.repositories.interfaces.MailUserRepository
import de.thm.mni.mail_backend.storage.FileStorageService
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

/**
 * Service for managing file attachments.
 * Handles uploading and downloading attachments with database persistence.
 */
@Service
class AttachmentService(
    private val attachmentRepository: AttachmentRepository,
    private val fileStorageService: FileStorageService,
    private val mailRepository: MailRepository,
    private val mailUserRepository: MailUserRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Helper Function: Checks if an Attachment is attached to a Mail belonging to the currently logged-in User
     * @param attachmentId The ID of the Attachment to check
     * @param currentUserId The ID of the currently logged-in User
     * @throws NoSuchElementException if no Mail with the Attachment was found or User does not own the Mail
     */
    fun checkUserHasAttachment(attachmentId: UUID, currentUserId: UUID) {

        val mailId = mailRepository.findByAttachmentsAttachmentId(attachmentId)?.mailId ?: throw NoSuchElementException("Mail not found!")

        mailUserRepository
            .findByUserIdAndMailId(currentUserId, mailId)
            .orElseThrow {
                NoSuchElementException(
                    "Mail not found!"
                )
            }
    }

    @Transactional
    fun saveAttachments(files: List<MultipartFile>): List<AttachmentResponse> {
        logger.info("Starting to save {} attachment(s)", files.size)
        return files.map { file ->
            // store file
            val savePath = fileStorageService.store(file)

            // create attachment DTO from file
            val attachmentCreate = AttachmentCreate.fromFile(file, savePath.toString())
            
            // convert to entity and save
            val attachment = attachmentCreate.toAttachment()
            val saved = attachmentRepository.save(attachment)
            
            logger.debug("Attachment saved with ID: {} ({})", saved.attachmentId, file.originalFilename)

            // convert to response
            saved.toResponse()
        }.also { 
            logger.info("Successfully saved {} attachment(s)", it.size)
        }
    }

    fun loadFileAsResource(id: UUID, currentUserId: UUID): Pair<Attachment, Resource> {
        logger.debug("Loading file resource for attachment ID: {}", id)
        checkUserHasAttachment(id, currentUserId)
        val attachment = attachmentRepository.findById(id).orElseThrow {
            StorageFileNotFoundException("File not found with id $id") 
        }
        val resource = fileStorageService.loadAsResource(attachment.storageUrl)
        return Pair(attachment, resource)
    }

}
