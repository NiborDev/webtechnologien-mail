package de.thm.mni.mail_backend.exceptions

/**
 * Exception for storage related errors.
 * 
 * This is used to show problems that happen when something is being stored.
 * 
 * @property message The error message.
 * @property cause The cause of the exception.
 * @constructor Creates a new instance of StorageException.
 */
open class StorageException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)