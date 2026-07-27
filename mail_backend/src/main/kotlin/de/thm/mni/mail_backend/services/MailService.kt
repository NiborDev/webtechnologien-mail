package de.thm.mni.mail_backend.services

import de.thm.mni.mail_backend.data.Mail
import de.thm.mni.mail_backend.data.enums.EMailStatus
import de.thm.mni.mail_backend.dto.mail.MailUpdateData
import de.thm.mni.mail_backend.dto.mail.MailUpdateStatus
import de.thm.mni.mail_backend.repositories.MailRepository
import org.springframework.stereotype.Service
import java.util.UUID

//TODO auth, paging

@Service
class MailService(private val mailRepository: MailRepository) {

    /**
     * Creates a new Mail
     * @param mail The Mail to create
     * @return The Created Mail
     */
    fun createMail(mail: Mail): Mail {
        return mailRepository.save(mail)
    }

    /**
     * Deletes a Mail from the Database
     * @param mailId The ID of the mail to delete
     */
    fun deleteMail(mailId: UUID) {
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
        return mailRepository.findById(mailId).orElseThrow { NoSuchElementException("Mail not found!") }
    }

    /**
     * Gets all Mails that are from the current User
     * @return List of the Mails from that User
     */
    fun getAllFromUser(): List<Mail> {

        //TODO auth
        val mailAddress = "test@example.com"
        return mailRepository.findByFrom(mailAddress)
    }

    /**
     * Gets all Mails sent to thr current User (To, CC and BCC)
     * @return List of the Mails sent to that User (User E-Mail Address in To, Cc or Bcc and Status SENT)
     */
    fun getAllToUser(): List<Mail> {

        //TODO auth
        val mailAddress = "test@example.com"

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