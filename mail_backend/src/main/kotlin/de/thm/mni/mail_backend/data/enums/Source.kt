package de.thm.mni.mail_backend.data.enums

/**
 * Enum defining if a mail was created in the system or received externally
 * 
 * Possible values:
 * - INTERN: The mail was created in the system.
 * - EXTERN: The mail was received externally.
 */
enum class Source {
    INTERN,
    EXTERN,
}