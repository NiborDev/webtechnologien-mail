package de.thm.mni.mail_backend.services

import de.thm.mni.mail_backend.data.User
import de.thm.mni.mail_backend.repositories.interfaces.UserRepository
import de.thm.mni.mail_backend.security.PepperedPasswordEncoder
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import java.util.NoSuchElementException
import java.util.UUID

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PepperedPasswordEncoder
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val userNotFoundEncodedPassword = passwordEncoder.encode("userNotFoundPassword")

    /**
     * Registers a new user
     * 
     * @param user The user entity to register
     * @return The registered user
     */
    @Transactional
    fun registerUser(user: User): User {
        // Check if Email exists
        if (userRepository.existsByEmail(user.email)) {
            logger.warn("Registration failed: Email {} already exists", user.email)
            throw IllegalArgumentException("Email already exists")
        }

        user.password = passwordEncoder.encode(user.password)

        // Save the user to the database
        val savedUser = userRepository.save(user)
        logger.info("User registered successfully with ID {}", savedUser.userId)
        return savedUser
    }

    /**
     * Logins a user
     * 
     * @param email The user email address
     * @param rawPassword The raw plain text password
     * @return The logged in user
     */
    fun login(email: String, rawPassword: String): User {
        val user = userRepository.findByEmail(email)

        if (user == null) {
            logger.warn("Login failed: Email {} not found", userLogin.email)
            // Timing attacks prevention
            passwordEncoder.matches(rawPassword, userNotFoundEncodedPassword)
            throw IllegalArgumentException("Email or password incorrect")
        }

        if (!passwordEncoder.matches(rawPassword, user.password)) {
            logger.warn("Login failed: Invalid password for user {}", userLogin.email)
            throw IllegalArgumentException("Email or password incorrect")
        }

        logger.info("User {} authenticated successfully", user.userId)
        return user
    }

    /**
     * Returns all Users with pagination and optional search filter (firstName, lastName, email)
     * 
     * @param search Optional search query string
     * @param pageable Pageable object for paging
     * @return A page of users matching search criteria
     */
    fun getAllUsers(search: String?, pageable: Pageable): Page<User> {
        if (!search.isNullOrBlank()) {
            val query = search.trim()
            return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                query, query, query, pageable
            )
        }
        return userRepository.findAll(pageable)
    }

    /**
     * Returns a user by id
     * 
     * @param id The id of the user
     * @param currentUserId The id of the current user
     * @return The user with the given id
     */
    fun getUserById(id: UUID, currentUserId: UUID): User {
        if (id != currentUserId) {
            logger.warn("Access denied: User {} attempted to access user profile {}", currentUserId, id)
            throw AccessDeniedException("User $currentUserId attempted to access user $id")
        }
        return userRepository.findById(id).orElseThrow { NoSuchElementException("User not found") }
    }

    /**
     * Updates a user
     * 
     * @param id The id of the user
     * @param currentUserId The id of the current user
     * @param updatedUser The updated user entity
     * @return The updated user
     */
    @Transactional
    fun updateUser(id: UUID, currentUserId: UUID, updatedUser: User): User {
        // Check User have Permissions to update
        if (id != currentUserId) {
            logger.warn("Access denied: User {} attempted to update user {}", currentUserId, id)
            throw AccessDeniedException("User $currentUserId attempted to update user $id")
        }
        val existingUser = userRepository.findById(id).orElseThrow { NoSuchElementException("User not found") }

        // Check if Email exists
        if (updatedUser.email != existingUser.email && userRepository.existsByEmail(updatedUser.email)) {
            logger.warn("User update failed: Email {} already exists", updatedUser.email)
            throw IllegalArgumentException("Email already exists")
        }

        val savedUser = userRepository.save(updatedUser)
        logger.info("User {} updated profile successfully", id)
        return savedUser
    }

    /**
     * Updates a user's password
     * 
     * @param id The id of the user
     * @param currentUserId The id of the current user
     * @param oldPassword The current password for verification
     * @param newPassword The new password to set
     * @return The updated user
     */
    @Transactional
    fun updatePassword(id: UUID, currentUserId: UUID, oldPassword: String, newPassword: String): User {
        // Check User have Permissions to update
        if (id != currentUserId) {
            logger.warn("Access denied: User {} attempted to change password of user {}", currentUserId, id)
            throw AccessDeniedException("User $currentUserId attempted to change password of user $id")
        }
        val existingUser = userRepository.findById(id).orElseThrow { NoSuchElementException("User not found") }

        // Check if Password is correct
        if (!passwordEncoder.matches(oldPassword, existingUser.password)) {
            logger.warn("Password change failed: Incorrect old password for user {}", id)
            throw IllegalArgumentException("Password is incorrect")
        }

        val hashedPassword = passwordEncoder.encode(newPassword)
        existingUser.password = hashedPassword

        val savedUser = userRepository.save(existingUser)
        logger.info("Password updated successfully for user {}", id)
        return savedUser
    }

    /**
     * Deletes a user
     * 
     * @param id The id of the user
     * @param currentUserId The id of the current user
     */
    @Transactional
    fun deleteUser(id: UUID, currentUserId: UUID) {
        if (id != currentUserId) {
            logger.warn("Access denied: User {} attempted to delete user {}", currentUserId, id)
            throw AccessDeniedException("User $currentUserId attempted to delete user $id")
        }
        userRepository.deleteById(id)
        logger.info("User {} account deleted successfully", id)
    }

}