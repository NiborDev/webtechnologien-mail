package de.thm.mni.mail_backend.services

import de.thm.mni.mail_backend.data.Mail
import de.thm.mni.mail_backend.data.MailUser
import de.thm.mni.mail_backend.data.User
import de.thm.mni.mail_backend.data.enums.EMailStatus
import de.thm.mni.mail_backend.data.enums.MailUserRelation
import de.thm.mni.mail_backend.dto.mail.MailUpdateData
import de.thm.mni.mail_backend.dto.mail.MailUpdateStatus
import de.thm.mni.mail_backend.repositories.MailRepository
import de.thm.mni.mail_backend.repositories.MailUserRepository
import de.thm.mni.mail_backend.repositories.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

//TODO auth (get real data!), paging

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
     * Sets a Mail as deleted for the User currently logged in
     * @param mailId The ID of the mail to delete
     * @throws IllegalStateException if no User is logged in
     * @throws NoSuchElementException if Mail does not exist or User does not own the Mail
     */
    @Transactional
    fun deleteMailForUser(mailId: UUID) {

        val userId: UUID = getCurrentUser().userId ?: throw IllegalStateException("No Logged in User")

        val mailUser = mailUserRepository
            .findByUserIdAndMailId(userId, mailId)
            .orElseThrow { NoSuchElementException("Mail does not exist or User has no permissions to delete it!") }

        mailUser.isDeleted = true

        mailUserRepository.save(mailUser)
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
     * @return List of the Mails from that User
     */
    fun getAllFromUser(): List<Mail> {

        val mailAddress = getCurrentUser().email
        return mailRepository.findByFrom(mailAddress)
    }

    /**
     * Gets all Mails sent to thr current User (To, CC and BCC)
     * @return List of the Mails sent to that User (User E-Mail Address in To, Cc or Bcc and Status SENT)
     */
    fun getAllToUser(): List<Mail> {

        val mailAddress = getCurrentUser().email

        //get all máils that have the address in to/cc/bcc and are sent
        val mailsTo = mailRepository.findByToAndStatus(mailAddress, EMailStatus.SENT)
        val mailsCC = mailRepository.findByCcAndStatus(mailAddress, EMailStatus.SENT)
        val mailsBcc = mailRepository.findByBccAndStatus(mailAddress, EMailStatus.SENT)

        return (mailsTo + mailsCC + mailsBcc).distinctBy { it.mailId }

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

}