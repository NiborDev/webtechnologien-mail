package de.thm.mni.mail_backend

import de.thm.mni.mail_backend.config.StorageProperties
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@OpenAPIDefinition(
	info = Info(
		title = "Mail API",
		version = "1.0.0",
		description = "This API provides an API for our Mail application.",
	)
)
@SpringBootApplication
@EnableConfigurationProperties(StorageProperties::class)
class MailBackendApplication

fun main(args: Array<String>) {
	runApplication<MailBackendApplication>(*args)
}
