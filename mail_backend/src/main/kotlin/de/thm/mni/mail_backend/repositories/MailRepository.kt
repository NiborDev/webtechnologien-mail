package de.thm.mni.mail_backend.repositories

import de.thm.mni.mail_backend.data.Mail
import de.thm.mni.mail_backend.data.enums.EMailStatus
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MailRepository: CrudRepository<Mail, UUID> {
    fun findByFrom(from: String): List<Mail>
    fun findByToAndStatus(to: String, status: EMailStatus): List<Mail>
    fun findByCcAndStatus(cc: String, status: EMailStatus): List<Mail>
    fun findByBccAndStatus(bcc: String, status: EMailStatus): List<Mail>
}