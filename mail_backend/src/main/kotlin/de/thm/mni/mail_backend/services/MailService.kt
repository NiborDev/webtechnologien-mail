package de.thm.mni.mail_backend.services

import de.thm.mni.mail_backend.data.Mail
import de.thm.mni.mail_backend.data.MailUser
import de.thm.mni.mail_backend.data.User
import de.thm.mni.mail_backend.data.enums.EMailStatus
import de.thm.mni.mail_backend.data.enums.MailUserRelation
import de.thm.mni.mail_backend.dto.mail.MailUpdateData
import de.thm.mni.mail_backend.dto.mail.MailUpdateIsRead
import de.thm.mni.mail_backend.dto.mail.MailUpdateStatus
import de.thm.mni.mail_backend.repositories.interfaces.MailRepository
import de.thm.mni.mail_backend.repositories.interfaces.MailUserRepository
import de.thm.mni.mail_backend.repositories.interfaces.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

//TODO auth (get real data!)

@Service
class MailService(
    private val mailRepository: MailRepository,
    private val mailUserRepository: MailUserRepository,
    private val userRepository: UserRepository
) {

    //TODO for test purposes only!!
    private fun getCurrentUser(): User {
        return User(
            userId = UUID.fromString("00000000-0000-0000-0000-000000000001"),
            email = "test@example.com",
            firstName = "Test",
            lastName = "User"
        )
    }

    /**
     * Helper function: Checks if Mail is owned by the User currently logged in
     * @param mailId The ID of the Mail to check
     * @throws IllegalAccessException if User does not own the Mail
     * @throws IllegalStateException if no User is logged in
     */
    private fun checkUserHasMail(mailId: UUID) {

        val userId = getCurrentUser().userId
            ?: throw IllegalStateException("No Logged in User")

        mailUserRepository
            .findByUserIdAndMailId(userId, mailId)
            .orElseThrow {
                IllegalAccessException(
                    "User has no access to this mail"
                )
            }
    }

    /**
     * Creates a new Mail
     * @param mail The Mail to create
     * @return The Created Mail
     */
    @Transactional
    fun createMail(mail: Mail): Mail {
        val savedMail = mailRepository.save(mail)
        val user = userRepository.findByEmail(savedMail.from)?: throw NoSuchElementException("User with this Mail-Address doesn't exist")
        mailUserRepository.save(
            MailUser(
                mailId = savedMail.mailId?: throw IllegalArgumentException("Mail Id Should not be null"),
                userId = user.userId?: throw IllegalArgumentException("User Id Should not be null"),
                relationType = MailUserRelation.AUTHOR
            )
        )
        return savedMail
    }

    /**
     * Deletes a Mail
     * @param mailId The ID of the mail to delete
     * @throws IllegalStateException if no User is logged in
     */
    @Transactional
    fun deleteMail(mailId: UUID) {

        checkUserHasMail(mailId)

        val userId: UUID = getCurrentUser().userId ?: throw IllegalStateException("No Logged in User")

        mailUserRepository.deleteByUserIdAndMailId(userId, mailId)
        mailRepository.deleteById(mailId)
    }

    /**
     * Updates a existing Mail (Status can not be SENT)
     * @param mailId The ID of the existing Mail
     * @param mailUpdateData The Information to update
     * @return The updated Mail
     * @throws NoSuchElementException if there is no Mail with the given ID
     * @throws IllegalStateException if the Mail is already sent (status SENT, can not be updated)
     */
    fun updateMail(mailId: UUID,mailUpdateData: MailUpdateData): Mail {

        checkUserHasMail(mailId)

        val current = mailRepository.findById(mailId).orElseThrow { NoSuchElementException("Mail not found!") }

        if (current.status == EMailStatus.SENT) {
            throw IllegalStateException("A Sent Mail cannot be edited")
        }

        val updated = mailUpdateData.toMail(current)

        return mailRepository.save(updated)
    }

    /**
     * Gets a Mail by ID
     * @param mailId The ID of the Mail
     * @return The Mail
     * @throws NoSuchElementException if the Mail was not found
     */
    fun getById(mailId: UUID): Mail {
        checkUserHasMail(mailId)
        return mailRepository.findById(mailId).orElseThrow { NoSuchElementException("Mail not found!") }
    }

    /**
     * Gets all Mails that are from the current User
     * @param eMailStatus status of mails to select
     * @param pageable Pageable Object for Paging
     * @throws IllegalStateException if no User is logged in
     * @return List of the Mails from that User that have the selected Status
     */
    fun getAllFromUserWithStatus(eMailStatus: EMailStatus ,pageable: Pageable): Page<Mail> {

        val userId = getCurrentUser().userId ?: throw IllegalStateException("No Logged in User")
        return mailRepository.findByUserIdAndStatus(userId, eMailStatus, pageable)
    }

    /**
     * Updates the status of a Mail
     * @param mailId The ID of the existing Mail
     * @param mailUpdateStatus The Status to update to
     * @return The updated Mail
     * @throws IllegalArgumentException if the Mail does not exist
     * @throws IllegalStateException if the Mail is already sent (status SENT, can not be updated)
     * @throws IllegalArgumentException if the status to update to is null
     *
     */
    fun updateMailStatus(mailId: UUID, mailUpdateStatus: MailUpdateStatus) : Mail {

        checkUserHasMail(mailId)

        val current = mailRepository.findById(mailId).orElseThrow { NoSuchElementException("Mail not found!") }

        if (current.status == EMailStatus.SENT) {
            throw IllegalStateException("A Sent Mail cannot be edited")
        }

        if (mailUpdateStatus.status == null) {
            throw IllegalArgumentException("Status to update to can not be null")
        }

        val updated = current
        updated.status = mailUpdateStatus.status!!

       return mailRepository.save(updated)
    }

    /**
     * Updates the isRead status of a Mail
     * @param mailId The ID of the existing Mail
     * @param mailUpdateIsRead The Status to update to
     * @return The updated Mail
     * @throws IllegalArgumentException if the Mail does not exist
     * @throws IllegalArgumentException if the status to update to is null
     *
     */
    fun updateMailIsRead(mailId: UUID, mailUpdateIsRead: MailUpdateIsRead) : Mail {

        checkUserHasMail(mailId)

        val current = mailRepository.findById(mailId).orElseThrow { NoSuchElementException("Mail not found!") }

        if (mailUpdateIsRead.isRead == null) {
            throw IllegalArgumentException("Status to update to can not be null")
        }

        val updated = current
        updated.isRead = mailUpdateIsRead.isRead!!

        return mailRepository.save(updated)
    }

    /**
     * Mock-Function: Mocks the sending of a Mail (sends it to all Addresses that are internal to the System, ignores other Addresses)
     * @param mailID The ID of the Mail to send
     * @return mail the send Mail
     * @throws NoSuchElementException if the Mail doesnt exist
     */
    @Transactional
    fun sendMail(mailID: UUID): Mail{
        checkUserHasMail(mailID)

        val mail = mailRepository.findById(mailID).orElseThrow { NoSuchElementException("Mail not found!") }

        val recipients = (mail.to + mail.cc + mail.bcc)

        recipients.forEach {

            val user = userRepository.findByEmail(it)

            if (user != null) {
                receiveMail(mail, user)
            }

        }

        mail.status = EMailStatus.SENT
        return mailRepository.save(mail)

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
            source = mail.source
        )
        val savedMail = mailRepository.save(receivedMail)
        mailUserRepository.save(
            MailUser(
                mailId = savedMail.mailId ?: throw IllegalArgumentException("Mail Id Should not be null"),
                userId = user.userId ?: throw IllegalArgumentException("User Id Should not be null"),
                relationType = MailUserRelation.RECIPIENT
            )
        )

        return savedMail

    }

}