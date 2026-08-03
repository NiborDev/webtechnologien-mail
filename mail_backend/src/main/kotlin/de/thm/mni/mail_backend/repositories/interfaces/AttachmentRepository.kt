package de.thm.mni.mail_backend.repositories.interfaces

import de.thm.mni.mail_backend.data.Attachment
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AttachmentRepository : CrudRepository<Attachment, UUID>
