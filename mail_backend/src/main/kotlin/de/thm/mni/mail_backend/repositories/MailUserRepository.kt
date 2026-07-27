package de.thm.mni.mail_backend.repositories

import de.thm.mni.mail_backend.data.MailUser
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

interface MailUserRepository: CrudRepository<MailUser, UUID> {
    fun findByUserIdAndMailId(userId: UUID,mailId: UUID): Optional<MailUser>
}