package de.thm.mni.mail_backend.data.enums

/**
 * Enum representing the Status of a Mail
 * 
 * Possible values:
 * - DRAFT: The mail is a draft.
 * - SENT: The mail has been sent.
 * - RECEIVED: The mail has been received.
 * - ERROR: An error has occurred while sending the mail
 */
enum class EMailStatus {
    DRAFT,
    SENT,
    RECEIVED,
    ERROR,
}