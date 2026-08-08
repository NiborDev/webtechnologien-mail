package de.thm.mni.mail_backend.repositories.interfaces

import de.thm.mni.mail_backend.data.User
import java.util.UUID
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * Repository for User entities.
 * 
 * This repository is used to perform CRUD operations on User entities.
 */
@Repository
interface UserRepository : CrudRepository<User, UUID> {
    /**
     * Checks if a user with the given email exists.
     *
     * @param email The email to check for existence.
     * @return True if a user with the given email exists, false otherwise.
     */
    fun existsByEmail(email: String): Boolean
    
    /**
     * Finds a user by email.
     *
     * @param email The email to search for.
     * @return The user if found, otherwise null.
     */
    fun findByEmail(email: String): User?
    
    /**
     * Finds a user by email, ignoring case.
     *
     * @param email The email to search for.
     * @return The user if found, otherwise null.
     */
    fun findByEmailIgnoreCase(email: String): User?
    
    /**
     * Finds a user by user ID.
     *
     * @param userId The ID of the user to search for.
     * @return The user if found, otherwise null.
     */
    fun findByUserId(userId: UUID): User?
    
    /**
     * Finds all users with the given pagination information.
     *
     * @param pageable The pagination information.
     * @return A page of users that match the criteria.
     */
    fun findAll(pageable: Pageable): Page<User>

    /**
     * Finds all users whose first name, last name, or email contains the given string.
     *
     * @param firstName The first name to search for.
     * @param lastName The last name to search for.
     * @param email The email to search for.
     * @param pageable The pagination information.
     * @return A page of users that match the criteria.
     */
    fun findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
        firstName: String,
        lastName: String,
        email: String,
        pageable: Pageable
    ): Page<User>
}
