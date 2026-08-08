package de.thm.mni.mail_backend.data

import de.thm.mni.mail_backend.data.enums.MailUserRelation
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

/**
 * Entity representing the relation between a User and a Mail
 * @param id The ID of the entry
 * @param userId The ID of the User
 * @param mailId The ID of the Mail
 * @param relationType The relation type between the User and the Mail
 */
@Entity
@Table(name = "mail_user")
class MailUser (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id : UUID? = null,

    var userId: UUID,

    var mailId: UUID,

    @Enumerated(EnumType.STRING)
    var relationType: MailUserRelation
)