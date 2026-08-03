package de.thm.mni.mail_backend.services

import de.thm.mni.mail_backend.data.Attachment
import de.thm.mni.mail_backend.dto.attachments.AttachmentCreate
import de.thm.mni.mail_backend.dto.attachments.AttachmentResponse
import de.thm.mni.mail_backend.exceptions.StorageFileNotFoundException
import de.thm.mni.mail_backend.repositories.interfaces.AttachmentRepository
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
    private val fileStorageService: FileStorageService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
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

    fun loadFileAsResource(id: UUID): Pair<Attachment, Resource> {
        logger.debug("Loading file resource for attachment ID: {}", id)
        // TODO: Check Permission so only Allowed Users have access to the file!
        // We can store the userIds with access to the file or we check it over the mailId and check if the user have
        // permissions to the mail if so then the user also have access to the attached files
        val attachment = attachmentRepository.findById(id).orElseThrow { 
            logger.warn("Attachment not found with ID: {}", id)
            StorageFileNotFoundException("File not found with id $id") 
        }
        val resource = fileStorageService.loadAsResource(attachment.storageUrl)
        return Pair(attachment, resource)
    }

}
