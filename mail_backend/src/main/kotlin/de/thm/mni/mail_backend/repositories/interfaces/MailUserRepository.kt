package de.thm.mni.mail_backend.repositories.interfaces

import de.thm.mni.mail_backend.data.MailUser
import de.thm.mni.mail_backend.data.enums.MailUserRelation
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

/**
 * Repository for MailUser entities.
 * 
 * This repository is used to perform CRUD operations on MailUser entities.
 */
interface MailUserRepository: CrudRepository<MailUser, UUID> {
    /**
     * Finds a MailUser by user ID and mail ID.
     *
     * @param userId The ID of the user.
     * @param mailId The ID of the mail.
     * @return The MailUser if found, otherwise null.
     */
    fun findByUserIdAndMailId(userId: UUID,mailId: UUID): Optional<MailUser>
    
    /**
     * Finds a MailUser by user ID, mail ID, and relation type.
     *
     * @param userId The ID of the user.
     * @param mailId The ID of the mail.
     * @param relationType The relation type.
     * @return The MailUser if found, otherwise null.
     */
    fun findByUserIdAndMailIdAndRelationType(userId: UUID, mailId: UUID, relationType: MailUserRelation): Optional<MailUser>
    
    /**
     * Deletes a MailUser by user ID and mail ID.
     *
     * @param userId The ID of the user.
     * @param mailId The ID of the mail.
     */
    fun deleteByUserIdAndMailId(userId: UUID,mailId: UUID)
}