package de.thm.mni.mail_backend.exceptions

/**
 * Exception for when a storage file is not found.
 * 
 * This is used to show problems that happen when a file cannot be found in storage.
 * 
 * @property message The error message.
 * @property cause The cause of the exception.
 * @constructor Creates a new instance of StorageFileNotFoundException.
 */
class StorageFileNotFoundException(message: String, cause: Throwable? = null) : StorageException(message, cause)