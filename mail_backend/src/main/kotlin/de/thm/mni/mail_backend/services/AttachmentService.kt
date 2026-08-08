package de.thm.mni.mail_backend.services

import de.thm.mni.mail_backend.data.Attachment
import de.thm.mni.mail_backend.data.enums.EMailStatus
import de.thm.mni.mail_backend.data.enums.MailUserRelation
import de.thm.mni.mail_backend.exceptions.StorageFileNotFoundException
import de.thm.mni.mail_backend.repositories.interfaces.AttachmentRepository
import de.thm.mni.mail_backend.repositories.interfaces.MailRepository
import de.thm.mni.mail_backend.repositories.interfaces.MailUserRepository
import de.thm.mni.mail_backend.storage.FileStorageService
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.MimeType
import org.springframework.util.MimeTypeUtils
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
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
    private fun checkUserHasAttachment(attachmentId: UUID, currentUserId: UUID) {
        val mail = mailRepository.findByAttachmentsAttachmentId(attachmentId)
        if (mail == null) {
            val attachment = attachmentRepository.findById(attachmentId).orElseThrow {
                NoSuchElementException("Resource not found")
            }
            if (!attachment.storageUrl.contains(currentUserId.toString())) {
                logger.warn("User {} attempted to access unassigned attachment {} owned by another directory", currentUserId, attachmentId)
                throw NoSuchElementException("Resource not found")
            }
            return
        }

        mailUserRepository
            .findByUserIdAndMailId(currentUserId, mail.mailId!!)
            .orElseThrow {
                logger.warn("User {} attempted to access attachment {} without mail relation", currentUserId, attachmentId)
                NoSuchElementException("Resource not found")
            }
    }

    /**
     * Helper Function: Checks if an Attachment can be deleted by the user.
     * Verifies that the user is the author of the mail and that the mail is still a draft.
     *
     * @param attachmentId The ID of the Attachment to check
     * @param currentUserId The ID of the currently logged-in User
     * @throws AccessDeniedException if the user is not the author or the mail has already been sent
     */
    private fun checkUserCanDeleteAttachment(attachmentId: UUID, currentUserId: UUID) {
        val mail = mailRepository.findByAttachmentsAttachmentId(attachmentId)
        if (mail == null) {
            val attachment = attachmentRepository.findById(attachmentId).orElse(null) ?: return
            if (!attachment.storageUrl.contains(currentUserId.toString())) {
                logger.warn("User {} attempted to delete unassigned attachment {} owned by another directory", currentUserId, attachmentId)
                throw NoSuchElementException("Resource not found")
            }
            return
        }

        // Verify ownership: user must be AUTHOR of the mail
        val isAuthor = mailUserRepository
            .findByUserIdAndMailIdAndRelationType(currentUserId, mail.mailId!!, MailUserRelation.AUTHOR)
            .isPresent

        if (!isAuthor) {
            logger.warn("User {} attempted to delete attachment {} without AUTHOR relation", currentUserId, attachmentId)
            throw NoSuchElementException("Resource not found")
        }

        // Verify email status: email must be DRAFT or ERROR
        if ((mail.status != EMailStatus.DRAFT) || (mail.status != EMailStatus.ERROR)) {
            logger.warn("Attempted to delete attachment {} on mail {} with status {}", attachmentId, mail.mailId, mail.status)
            throw NoSuchElementException("Resource not found")
        }
    }

    /**
     * Stores a list of multipart files on disk in a user-specific folder and saves their corresponding metadata in the database.
     *
     * @param files The list of uploaded multipart files to process and store.
     * @param currentUserId The ID of the currently authenticated user uploading the files.
     * @return A list of [Attachment] entities representing the saved attachments.
     */
    @Transactional
    fun saveAttachments(files: List<MultipartFile>, currentUserId: UUID): List<Attachment> {
        logger.info("Starting to save {} attachment(s) for user {}", files.size, currentUserId)
        return files.map { file ->
            // store file in user folder
            val savePath = fileStorageService.store(file, currentUserId)
            val attachment = Attachment.fromFile(file, savePath.toString())
            val saved = attachmentRepository.save(attachment)
            
            logger.debug("Attachment saved with ID: {} ({}) for user {}", saved.attachmentId, file.originalFilename, currentUserId)
            saved
        }.also { 
            logger.info("Successfully saved {} attachment(s) for user {}", it.size, currentUserId)
        }
    }

    /**
     * Loads the stored file resource for an attachment if the current user has permission to access it.
     *
     * @param id The unique ID of the attachment to load.
     * @param currentUserId The ID of the currently authenticated user requesting access.
     * @return A [Pair] containing the [Attachment] metadata entity and the loaded file [Resource].
     * @throws NoSuchElementException If the attachment does not belong to a mail owned by the user.
     * @throws StorageFileNotFoundException If the attachment record exists but the underlying file resource is not found.
     */
    fun loadFileAsResource(id: UUID, currentUserId: UUID): Pair<Attachment, Resource> {
        logger.debug("Loading file resource for attachment ID: {}", id)
        checkUserHasAttachment(id, currentUserId)
        val attachment = attachmentRepository.findById(id).orElseThrow {
            StorageFileNotFoundException("File not found with id $id") 
        }
        val resource = fileStorageService.loadAsResource(attachment.storageUrl)
        return Pair(attachment, resource)
    }

    /**
     * Deletes an attachment idempotently if the user is the author/owner and the associated email has not yet been sent.
     * Any non-existent file or authorization error during deletion is handled gracefully.
     *
     * @param id The unique ID of the attachment to delete.
     * @param currentUserId The ID of the currently authenticated user requesting deletion.
     */
    @Transactional
    fun deleteAttachment(id: UUID, currentUserId: UUID) {
        logger.info("Delete request for attachment ID: {} by user ID: {}", id, currentUserId)

        try {
            val attachment = attachmentRepository.findById(id).orElse(null)
            if (attachment == null) {
                logger.info("Attachment ID: {} not found or already deleted (idempotent deletion)", id)
                return
            }

            checkUserCanDeleteAttachment(id, currentUserId)

            val mail = mailRepository.findByAttachmentsAttachmentId(id)
            if (mail != null) {
                mail.attachments.removeIf { it.attachmentId == id }
                mailRepository.save(mail)
            }

            attachmentRepository.delete(attachment)
            fileStorageService.delete(attachment.storageUrl)
            logger.info("Successfully deleted attachment ID: {}", id)
        } catch (e: Exception) {
            logger.warn("Idempotent attachment deletion handling for attachment {}: {}", id, e.message)
        }
    }

}
