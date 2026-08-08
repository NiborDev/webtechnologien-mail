package de.thm.mni.mail_backend.repositories.interfaces

import de.thm.mni.mail_backend.data.Mail
import de.thm.mni.mail_backend.data.enums.EMailStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * Repository for Mail entities.
 * 
 * This repository is used to perform CRUD operations on Mail entities.
 */
@Repository
interface MailRepository: CrudRepository<Mail, UUID> {
    /**
     * Finds all mails of a user with a specific status.
     *
     * @param userId The ID of the user.
     * @param eMailStatus The status of the mail.
     * @param pageable The pagination information.
     * @return A page of mails that match the criteria.
     */
    @Query("""
        SELECT m
        FROM Mail m
        JOIN MailUser mu ON mu.mailId = m.mailId
        WHERE mu.userId = :userId
        AND m.status = :emailStatus
    """)
    fun findByUserIdAndStatus(@Param("userId") userId: UUID,@Param("emailStatus") eMailStatus: EMailStatus , pageable: Pageable): Page<Mail>
    
    /**
     * Finds a mail by its attachment ID.
     *
     * @param attachmentId The ID of the attachment.
     * @return The mail that contains the attachment, or null if not found.
     */
    fun findByAttachmentsAttachmentId(@Param("attachmentId") attachmentId: UUID ): Mail?
}