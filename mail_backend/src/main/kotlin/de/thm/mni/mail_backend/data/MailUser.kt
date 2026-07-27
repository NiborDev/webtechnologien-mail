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

@Entity
@Table(name = "mail_user")
data class MailUser (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id : UUID? = null,

    var userId: UUID,

    var mailId: UUID,

    var isDeleted: Boolean = false,
    var isRead : Boolean = false,

    @Enumerated(EnumType.STRING)
    var relationType: MailUserRelation
)