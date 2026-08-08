package de.thm.mni.mail_backend.controller

import de.thm.mni.mail_backend.annotations.DefaultApiErrors
import de.thm.mni.mail_backend.data.enums.EMailStatus
import de.thm.mni.mail_backend.dto.mail.MailCreate
import de.thm.mni.mail_backend.dto.mail.MailResponse
import de.thm.mni.mail_backend.dto.mail.MailUpdateData
import de.thm.mni.mail_backend.dto.mail.MailUpdateIsRead
import de.thm.mni.mail_backend.services.MailService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
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
@Tag(name = "Mails",
    description = "Manages API for Mails including creating, reading, updating, deleting, sending and receiving Mails",)
@DefaultApiErrors
@RestController
@RequestMapping("/mails")
class MailController(val mailService: MailService) {

    /**
     * POST /mails creates a new Mail
     * @param mailCreate The Mail to create
     * @param userDetails User Data from JWT
     * @return The created Mail
     */
    @Operation(
        operationId = "createMail",
        summary = "create a new Mail",
        description = "This Function creates a new Mail, sets the sender (from) and creates relations to attachments and Users"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Mail creation successful.")
        ]
    )
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
    @Operation(
        operationId = "deleteMail",
        summary = "Deletes a Mail",
        description = "This function deletes a Mail from the System."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "User deleted successfully.")
        ]
    )
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
    @Operation(
        operationId = "updateMailData",
        summary = "Updates a Mail",
        description = "This function updates a Mail including recipients, subject, attachments and bodyText."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Mail successfully updated.")
        ]
    )
    @PatchMapping("/{id}")
    fun updateMailData(@PathVariable id: UUID, @Valid @RequestBody mailUpdateData: MailUpdateData, @AuthenticationPrincipal userDetails: UserDetails ): MailResponse {
        val mail = mailService.updateMail(id, mailUpdateData, UUID.fromString(userDetails.username))
        return mail.toResponse()
    }

    /**
     * PATCH /mails/{id}/isRead updates the isRead status of a Mail
     * @param id The ID of the Mail to update
     * @param mailUpdateIsRead The value to update to
     * @param userDetails User Data from JWT
     * @return The updated Mail
     */
    @Operation(
        operationId = "updateMailIsRead",
        summary = "Updates the isRead status of a Mail",
        description = "This function updates the isRead status of a Mail from the System."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "isRead status successfully updated.")
        ]
    )
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
    @Operation(
        operationId = "getMailById",
        summary = "gets a single mail",
        description = "This function gets a single mail with a specific ID from the System."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Mail retrieved successfully.")
        ]
    )
    @GetMapping("/{id}")
    fun getMailById(@PathVariable id: UUID, @AuthenticationPrincipal userDetails: UserDetails): MailResponse {
        val mail = mailService.getById(id, UUID.fromString(userDetails.username))
        return mail.toResponse()
    }

    /**
     * GET /mails gets all Mails from the current User with the selected Status as a paginated List
     * @param status RequestParam for the selected Status
     * @param pageable Pageable Object for paging
     * @param userDetails User Data from JWT
     * @returns A Page of the Mails from the current User
     */
    @Operation(
        operationId = "getMails",
        summary = "gets all mails of the current User",
        description = "This function retrieves all mails of the current User as a paginated List"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Mails retrieved successfully.")
        ]
    )
    @GetMapping
    fun getMails(
        @RequestParam status: EMailStatus,
        @ParameterObject pageable: Pageable,
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
    @Operation(
        operationId = "sendMail",
        summary = "sends a mail",
        description = "This function triggers the send operation for a mail in the system."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Mail successfully send.")
        ]
    )
    @PostMapping("/{id}/transmissions")
    fun sendMail(@PathVariable id: UUID, @AuthenticationPrincipal userDetails: UserDetails): MailResponse{
        return mailService.sendMail(id, UUID.fromString(userDetails.username)).toResponse()
    }
}