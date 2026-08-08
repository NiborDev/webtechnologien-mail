package de.thm.mni.mail_backend.repositories.interfaces

import de.thm.mni.mail_backend.data.Attachment
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * Repository for Attachment entities.
 * 
 * This repository is used to perform CRUD operations on Attachment entities.
 */
@Repository
interface AttachmentRepository : CrudRepository<Attachment, UUID>
