package de.thm.mni.mail_backend.data

import de.thm.mni.mail_backend.data.enums.EMailStatus
import de.thm.mni.mail_backend.dto.mail.MailResponse
import jakarta.persistence.CascadeType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

//TODO: finale implementierung von source

@Entity
@Table(name = "mails")
data class Mail(
    @Id
    var mailId: UUID? = null,

    @Column(name = "from_email")
    var from: String = "",

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "mail_to_addresses", joinColumns = [JoinColumn(name = "mail_id")])
    @Column(name = "to_email")
    var to: MutableList<String> = mutableListOf(),

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "mail_cc_addresses", joinColumns = [JoinColumn(name = "mail_id")])
    @Column(name = "cc_email")
    var cc: MutableList<String> = mutableListOf(),

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "mail_bcc_addresses", joinColumns = [JoinColumn(name = "mail_id")])
    @Column(name = "bcc_email")
    var bcc: MutableList<String> = mutableListOf(),

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "mail_reply_to_addresses", joinColumns = [JoinColumn(name = "mail_id")])
    @Column(name = "reply_to_email")
    var replyTo: MutableList<String> = mutableListOf(),

    var subject: String = "",
    var bodyText: String = "",

    @Enumerated(EnumType.STRING)
    var status: EMailStatus = EMailStatus.DRAFT,

    var isRead: Boolean = false,
    var isMarked: Boolean = false,
    var sentAt: LocalDateTime? = null,
    var receivedAt: LocalDateTime? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "mail_id")
    var attachments: MutableList<Attachments> = mutableListOf(),
    
    var source: String = "INTERNAL",
) {
    fun toResponse(): MailResponse {
        return MailResponse(
            mailId = requireNotNull(mailId) { "mailId must not be null" },
            from = from,
            to = to,
            cc = cc,
            bcc = bcc,
            replyTo = replyTo,
            subject = subject,
            bodyText = bodyText,
            status = status,
            attachments = attachments,
            isRead = isRead,
            isMarked = isMarked,
            sentAt = sentAt,
            receivedAt = receivedAt,
            source = source
        )
    }
}
