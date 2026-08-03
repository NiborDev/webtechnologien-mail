package de.thm.mni.mail_backend.repositories

import de.thm.mni.mail_backend.data.Mail
import de.thm.mni.mail_backend.data.enums.EMailStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MailRepository: CrudRepository<Mail, UUID> {
    @Query("""
        SELECT m
        FROM Mail m
        JOIN MailUser mu ON mu.mailId = m.mailId
        WHERE mu.userId = :userId
        AND m.status = :emailStatus
    """)
    fun findByUserIdAndStatus(@Param("userId") userId: UUID,@Param("emailStatus") eMailStatus: EMailStatus , pageable: Pageable): Page<Mail>

    fun findByFrom(from: String, pageable: Pageable): Page<Mail>
}