package de.thm.mni.mail_backend

import de.thm.mni.mail_backend.config.StorageProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties::class)
class MailBackendApplication

fun main(args: Array<String>) {
	runApplication<MailBackendApplication>(*args)
}
