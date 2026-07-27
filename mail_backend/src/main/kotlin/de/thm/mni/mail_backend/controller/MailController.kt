package de.thm.mni.mail_backend.controller

import de.thm.mni.mail_backend.annotations.DefaultApiErrors
import de.thm.mni.mail_backend.data.Mail
import de.thm.mni.mail_backend.dto.mail.MailCreate
import de.thm.mni.mail_backend.dto.mail.MailResponse
import de.thm.mni.mail_backend.dto.mail.MailUpdateData
import de.thm.mni.mail_backend.dto.mail.MailUpdateStatus
import de.thm.mni.mail_backend.services.MailService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID


@DefaultApiErrors
@RestController
@RequestMapping("/mails")
class MailController(val mailService: MailService) {

    /**
     * POST /mails creates a new Mail
     * @param mailCreate The Mail to create
     * @return The created Mail
     */
    @PostMapping
    fun createMail(@Valid @RequestBody mailCreate: MailCreate): MailResponse {
        val mail = mailService.createMail(mailCreate.toMail())
        return mail.toResponse()
    }

    /**
     * DELETE /mails/{id} deletes a Mail
     * @param id The ID of the Mail to delete
     */
    @DeleteMapping("/{id}")
    fun deleteMail(@PathVariable id: UUID) {
        mailService.deleteMailForUser(id)
    }

    /**
     * PATCH /mails/{id} updates the data of a Mail
     * @param id The ID of the Mail to Update
     * @param mailUpdateData The Data to update to
     * @return The updated Mail
     */
    @PatchMapping("/{id}")
    fun updateMailData(@PathVariable id: UUID, @Valid @RequestBody mailUpdateData: MailUpdateData ): MailResponse {
        val mail = mailService.updateMail(id, mailUpdateData)
        return mail.toResponse()
    }

    /**
     * PATCH /mails/{id}/status updates the status of a Mail
     * @param id The ID of the Mail to update
     * @param mailUpdateStatus The status to update to
     * @return The updated Mail
     */
    @PatchMapping("/{id}/status")
    fun updateMailStatus(@PathVariable id: UUID, @Valid @RequestBody mailUpdateStatus: MailUpdateStatus): MailResponse {
        val mail = mailService.updateMailStatus(id, mailUpdateStatus)
        return mail.toResponse()
    }

    /**
     * GET /mails/{id} gets a Mail with a certain ID
     * @param id The ID of the Mail
     * @return The Mail
     */
    @GetMapping("/{id}")
    fun getMailById(@PathVariable id: UUID): MailResponse {
        val mail = mailService.getById(id)
        return mail.toResponse()
    }

    /**
     * GET /mails gets all Mails from the current User
     * @return A List of the Mails from the current User
     */
    @GetMapping
    fun getAllMailsFromUser(): List<MailResponse> {
        val mails = mailService.getAllFromUser()
        return mails.map { it.toResponse() }
    }

    /**
     * GET /mails/inbox gets all Mails addressed to the current User (To, Cc or Bcc)
     * @return A List of the Mails addressed to the current User
     */
    @GetMapping("/inbox")
    fun getAllMailsToUser(): List<MailResponse> {
        val mails = mailService.getAllToUser()
        return mails.map { it.toResponse() }
    }
}