package de.thm.mni.mail_backend.repositories.interfaces

import de.thm.mni.mail_backend.data.User
import java.util.UUID
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, UUID> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): User?
    fun findByEmailIgnoreCase(email: String): User?
    fun findByUserId(userId: UUID): User?
    fun findAll(pageable: Pageable): Page<User>
    fun findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
        firstName: String,
        lastName: String,
        email: String,
        pageable: Pageable
    ): Page<User>
}
