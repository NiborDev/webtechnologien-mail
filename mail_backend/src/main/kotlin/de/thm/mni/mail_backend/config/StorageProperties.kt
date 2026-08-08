package de.thm.mni.mail_backend.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Configuration properties for file storage.
 * Used to configure the file storage location.
 * 
 * @property location The root directory for file storage. Default is "uploads/attachments".
 */
@ConfigurationProperties(prefix = "app.storage")
data class StorageProperties (
    var location: String = "uploads/attachments"
)
