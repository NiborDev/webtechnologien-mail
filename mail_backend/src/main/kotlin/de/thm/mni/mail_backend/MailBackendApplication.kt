package de.thm.mni.mail_backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MailBackendApplication

fun main(args: Array<String>) {
	runApplication<MailBackendApplication>(*args)
}
