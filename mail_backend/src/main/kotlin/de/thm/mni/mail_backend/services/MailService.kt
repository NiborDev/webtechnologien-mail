package de.thm.mni.mail_backend.services

import de.thm.mni.mail_backend.data.Mail
import de.thm.mni.mail_backend.data.MailUser
import de.thm.mni.mail_backend.data.User
import de.thm.mni.mail_backend.data.enums.EMailStatus
import de.thm.mni.mail_backend.data.enums.MailUserRelation
import de.thm.mni.mail_backend.data.enums.Source
import de.thm.mni.mail_backend.repositories.interfaces.AttachmentRepository
import de.thm.mni.mail_backend.repositories.interfaces.MailRepository
import de.thm.mni.mail_backend.repositories.interfaces.MailUserRepository
import de.thm.mni.mail_backend.repositories.interfaces.UserRepository
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

/**
 * Service for Mail entities.
 * 
 * This service is used to perform the business logic for mail management.
 */
@Service
class MailService(
    private val mailRepository: MailRepository,
    private val mailUserRepository: MailUserRepository,
    private val userRepository: UserRepository,
    private val attachmentRepository: AttachmentRepository,
    private val validator: Validator
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Helper function: Checks if Mail is owned by the currently logged-in User
     * @param mailId The ID of the Mail to check
     * @param currentUserId The ID of the currently logged-in User
     * @throws NoSuchElementException if User does not own the Mail
     */
    private fun checkUserHasMail(mailId: UUID, currentUserId: UUID) {

        mailUserRepository
            .findByUserIdAndMailId(currentUserId, mailId)
            .orElseThrow {
                logger.warn("Access denied: User {} attempted to access mail {}", currentUserId, mailId)
                NoSuchElementException(
                    "Mail not found!"
                )
            }
    }

    /**
     * Creates a new Mail
     * @param mail The Mail to create
     * @param currentUserId The ID of the currently logged-in User
     * @return The Created Mail
     * @throws IllegalArgumentException if mailId or userId is null
     * @throws NoSuchElementException if the User with the ID currentUserId doesn't exist
     */
    @Transactional
    fun createMail(mail: Mail, currentUserId: UUID): Mail {
        val user = userRepository.findByUserId(currentUserId)?: throw NoSuchElementException("User with this Mail-Address doesn't exist")
        mail.from = user.email

        // Maps new Attachments to entities from database (Existing Attachments are already entities)
        if (mail.attachments.isNotEmpty()) {
            val attachmentIds = mail.attachments.mapNotNull { it.attachmentId }
            val loadedAttachments = attachmentRepository.findAllById(attachmentIds)
            mail.attachments = loadedAttachments.toMutableList()
        }

        val savedMail = mailRepository.save(mail)
        mailUserRepository.save(
            MailUser(
                mailId = savedMail.mailId?: throw IllegalArgumentException("Mail Id Should not be null"),
                userId = user.userId?: throw IllegalArgumentException("User Id Should not be null"),
                relationType = MailUserRelation.AUTHOR
            )
        )
        logger.info("Created new draft mail {} for user {}", savedMail.mailId, currentUserId)
        return savedMail
    }

    /**
     * Deletes a Mail
     * @param mailId The ID of the mail to delete
     * @param currentUserId The ID of the currently logged-in User
     */
    @Transactional
    fun deleteMail(mailId: UUID, currentUserId: UUID) {

        checkUserHasMail(mailId, currentUserId)

        mailUserRepository.deleteByUserIdAndMailId(currentUserId, mailId)
        mailRepository.deleteById(mailId)
        logger.info("Deleted mail {} for user {}", mailId, currentUserId)
    }

    /**
     * Updates a existing Mail (DRAFT)
     * @param mailId The ID of the existing Mail
     * @param updatedMail The updated Mail entity
     * @param currentUserId The ID of the currently logged-in User
     * @return The updated Mail
     * @throws NoSuchElementException if there is no Mail with the given ID
     * @throws IllegalStateException if the Mail is not a draft (status SENT/RECEIVED, can not be updated)
     */
    @Transactional
    fun updateMail(mailId: UUID, updatedMail: Mail, currentUserId: UUID): Mail {

        checkUserHasMail(mailId, currentUserId)

        val current = mailRepository.findById(mailId).orElseThrow { NoSuchElementException("Mail not found!") }

        if (current.status != EMailStatus.DRAFT) {
            logger.warn("Update failed: Mail {} is not a DRAFT (current status: {})", mailId, current.status)
            throw IllegalStateException("A Mail can only be edited if its a draft")
        }

        // Maps new Attachments to entities from database (Existing Attachments are already entities)
        if (updatedMail.attachments.isNotEmpty()) {
            val attachmentIds = updatedMail.attachments.mapNotNull { it.attachmentId }
            val loadedAttachments = attachmentRepository.findAllById(attachmentIds)
            updatedMail.attachments = loadedAttachments.toMutableList()
        }

        val savedMail = mailRepository.save(updatedMail)
        logger.info("Updated draft mail {} for user {}", mailId, currentUserId)
        return savedMail
    }

    /**
     * Gets a Mail by ID
     * @param mailId The ID of the Mail
     * @param currentUserId The ID of the currently logged-in User
     * @return The Mail
     * @throws NoSuchElementException if the Mail was not found
     */
    fun getById(mailId: UUID, currentUserId: UUID): Mail {
        checkUserHasMail(mailId, currentUserId)
        return mailRepository.findById(mailId).orElseThrow { NoSuchElementException("Mail not found!") }
    }

    /**
     * Gets all Mails that are from the current User and have the selected Status (with Pagination)
     * @param eMailStatus status of mails to select
     * @param pageable Pageable Object for Paging
     * @param currentUserId The ID of the currently logged-in User
     * @return Page of the Mails from that User that have the selected Status
     */
    fun getAllFromUserWithStatus(eMailStatus: EMailStatus ,pageable: Pageable, currentUserId: UUID): Page<Mail> {

        return mailRepository.findByUserIdAndStatus(currentUserId, eMailStatus, pageable)
    }


    /**
     * Updates the isRead status of a Mail
     * @param mailId The ID of the existing Mail
     * @param isRead The status to update to
     * @param currentUserId The ID of the currently logged-in User
     * @return The updated Mail
     * @throws NoSuchElementException if the Mail does not exist
     */
    fun updateMailIsRead(mailId: UUID, isRead: Boolean, currentUserId: UUID) : Mail {

        checkUserHasMail(mailId, currentUserId)

        val current = mailRepository.findById(mailId).orElseThrow { NoSuchElementException("Mail not found!") }

        current.isRead = isRead

        return mailRepository.save(current)
    }

    /**
     * Mock-Function: Mocks the sending of a Mail (sends it to all Addresses that are internal to the System, ignores other Addresses)
     * @param mailID The ID of the Mail to send
     * @param currentUserId The ID of the currently logged-in User
     * @return mail the send Mail
     * @throws NoSuchElementException if the Mail doesnt exist
     * @throws IllegalStateException if the Mail is not a DRAFT
     * @throws ConstraintViolationException if the validation of the Mail fails
     */
    @Transactional
    fun sendMail(mailID: UUID, currentUserId: UUID): Mail{
        checkUserHasMail(mailID, currentUserId)

        val mail = mailRepository.findById(mailID).orElseThrow { NoSuchElementException("Mail not found!") }

        val validationErrors = validator.validate(mail)

        if (validationErrors.isNotEmpty()) {
            logger.warn("Send failed: Mail {} failed validation with {} errors", mailID, validationErrors.size)
            throw ConstraintViolationException(validationErrors)
        }

        if (mail.status != EMailStatus.DRAFT) {
            logger.warn("Send failed: Mail {} is not a DRAFT (status: {})", mailID, mail.status)
            throw IllegalStateException("A Mail can only be send if its a draft")
        }

        val recipients = (mail.to + mail.cc + mail.bcc).distinct()

        mail.bcc = mutableListOf() //remove bcc before sending
        mail.sentAt = LocalDateTime.now()

        recipients.forEach {

            val recipient = userRepository.findByEmailIgnoreCase(it)

            if (recipient != null) {
                receiveMail(mail, recipient)
            }

        }

        mail.status = EMailStatus.SENT
        val sentMail = mailRepository.save(mail)
        logger.info("Mail {} successfully sent to {} recipients by user {}", mailID, recipients.size, currentUserId)
        return sentMail

    }

    /**
     * Mock-Function: Mocks the Process of Receiving an Email
     * @param mail the Mail to receive
     * @param user the recipient
     * @return The received Mail
     * @throws IllegalArgumentException if mailId or userId is null
     */
    private fun receiveMail(mail: Mail, user: User): Mail {
        val receivedMail = Mail(
            mailId = UUID.randomUUID(),
            from = mail.from,
            to = mail.to.toMutableList(),
            cc = mail.cc.toMutableList(),
            bcc = mail.bcc.toMutableList(),
            replyTo = mail.replyTo.toMutableList(),
            subject = mail.subject,
            bodyText = mail.bodyText,
            status = EMailStatus.RECEIVED,
            isRead = false,
            isMarked = false,
            sentAt = mail.sentAt,
            receivedAt = LocalDateTime.now(),
            attachments = mail.attachments.toMutableList(),
            source = Source.EXTERN
        )
        val savedMail = mailRepository.save(receivedMail)
        mailUserRepository.save(
            MailUser(
                mailId = savedMail.mailId ?: throw IllegalArgumentException("Mail Id Should not be null"),
                userId = user.userId ?: throw IllegalArgumentException("User Id Should not be null"),
                relationType = MailUserRelation.RECIPIENT
            )
        )
        logger.info("Received mail {} assigned to user {}", savedMail.mailId, user.userId)

        return savedMail

    }

}