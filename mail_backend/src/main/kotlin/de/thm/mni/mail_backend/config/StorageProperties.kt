package de.thm.mni.mail_backend.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.storage")
data class StorageProperties (
    var location: String = "uploads/attachments"
)
