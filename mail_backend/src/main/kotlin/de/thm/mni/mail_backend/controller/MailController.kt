package de.thm.mni.mail_backend.controller

import de.thm.mni.mail_backend.annotations.DefaultApiErrors
import de.thm.mni.mail_backend.data.enums.EMailStatus
import de.thm.mni.mail_backend.dto.mail.MailCreate
import de.thm.mni.mail_backend.dto.mail.MailResponse
import de.thm.mni.mail_backend.dto.mail.MailUpdateData
import de.thm.mni.mail_backend.dto.mail.MailUpdateIsRead
import de.thm.mni.mail_backend.dto.mail.MailUpdateStatus
import de.thm.mni.mail_backend.services.MailService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID


import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

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
    @ResponseStatus(HttpStatus.CREATED)
    fun createMail(@Valid @RequestBody mailCreate: MailCreate, @AuthenticationPrincipal userDetails: UserDetails): MailResponse {
        val mail = mailService.createMail(mailCreate.toMail(), UUID.fromString(userDetails.username))
        return mail.toResponse()
    }

    /**
     * DELETE /mails/{id} deletes a Mail
     * @param id The ID of the Mail to delete
     * @param userDetails User Data from JWT
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMail(@PathVariable id: UUID, @AuthenticationPrincipal userDetails: UserDetails) {
        mailService.deleteMail(id, UUID.fromString(userDetails.username))
    }

    /**
     * PATCH /mails/{id} updates the data of a Mail
     * @param id The ID of the Mail to Update
     * @param mailUpdateData The Data to update to
     * @param userDetails User Data from JWT
     * @return The updated Mail
     */
    @PatchMapping("/{id}")
    fun updateMailData(@PathVariable id: UUID, @Valid @RequestBody mailUpdateData: MailUpdateData, @AuthenticationPrincipal userDetails: UserDetails ): MailResponse {
        val mail = mailService.updateMail(id, mailUpdateData, UUID.fromString(userDetails.username))
        return mail.toResponse()
    }

    /**
     * PATCH /mails/{id}/status updates the status of a Mail
     * @param id The ID of the Mail to update
     * @param mailUpdateStatus The status to update to
     * @param userDetails User Data from JWT
     * @return The updated Mail
     */
    @PatchMapping("/{id}/status")
    fun updateMailStatus(@PathVariable id: UUID, @Valid @RequestBody mailUpdateStatus: MailUpdateStatus, @AuthenticationPrincipal userDetails: UserDetails): MailResponse {
        val mail = mailService.updateMailStatus(id, mailUpdateStatus, UUID.fromString(userDetails.username))
        return mail.toResponse()
    }

    @PatchMapping("/{id}/isRead")
    fun updateMailIsRead(@PathVariable id: UUID, @Valid @RequestBody mailUpdateIsRead: MailUpdateIsRead, @AuthenticationPrincipal userDetails: UserDetails): MailResponse {
        val mail = mailService.updateMailIsRead(id, mailUpdateIsRead, UUID.fromString(userDetails.username))
        return mail.toResponse()
    }

    /**
     * GET /mails/{id} gets a Mail with a certain ID
     * @param id The ID of the Mail
     * @param userDetails User Data from JWT
     * @return The Mail
     */
    @GetMapping("/{id}")
    fun getMailById(@PathVariable id: UUID, @AuthenticationPrincipal userDetails: UserDetails): MailResponse {
        val mail = mailService.getById(id, UUID.fromString(userDetails.username))
        return mail.toResponse()
    }

    /**
     * GET /mails gets all Mails from the current User with the selected Status
     * @param status RequestParam for the selected Status
     * @param pageable Pageable Object for paging
     * @param userDetails User Data from JWT
     * @return A List of the Mails from the current User
     */
    @GetMapping
    fun getMails(
        @RequestParam status: EMailStatus,
        pageable: Pageable,
        @AuthenticationPrincipal userDetails: UserDetails
        ): Page<MailResponse> {

        return mailService
            .getAllFromUserWithStatus(status, pageable, UUID.fromString(userDetails.username))
            .map { it.toResponse() }
    }

    /**
     * POST /mails/{id}/transmissions triggers sending a Mail
     * @param id the ID of the Mail to send
     * @param userDetails User Data from JWT
     * @return the send mail
     */
    @PostMapping("/{id}/transmissions")
    fun sendMail(@PathVariable id: UUID, @AuthenticationPrincipal userDetails: UserDetails): MailResponse{
        return mailService.sendMail(id, UUID.fromString(userDetails.username)).toResponse()
    }
}