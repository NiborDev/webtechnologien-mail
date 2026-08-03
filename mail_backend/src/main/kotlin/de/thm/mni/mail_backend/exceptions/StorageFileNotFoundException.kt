package de.thm.mni.mail_backend.exceptions

class StorageFileNotFoundException(message: String, cause: Throwable? = null) : StorageException(message, cause)