package de.thm.mni.mail_backend.data.enums

/**
 * Enum representing the relation between a User and a Mail
 * 
 * Possible values:
 * - AUTHOR: The user is the author of the mail.
 * - RECIPIENT: The user is a recipient of the mail.
 */
enum class MailUserRelation {
    AUTHOR,
    RECIPIENT,
}