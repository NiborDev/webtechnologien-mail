package de.thm.mni.mail_backend.services

import de.thm.mni.mail_backend.data.User
import de.thm.mni.mail_backend.dto.user.UserCreate
import de.thm.mni.mail_backend.dto.user.UserLogin
import de.thm.mni.mail_backend.dto.user.UserUpdate
import de.thm.mni.mail_backend.dto.user.UserPasswordUpdate
import de.thm.mni.mail_backend.repositories.interfaces.UserRepository
import de.thm.mni.mail_backend.security.PepperedPasswordEncoder
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import java.util.NoSuchElementException
import java.util.UUID

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PepperedPasswordEncoder
) {
    private val userNotFoundEncodedPassword = passwordEncoder.encode("userNotFoundPassword")

    /**
     * Registers a new user
     * 
     * @param user The user to register
     * @return The registered user
     */
    @Transactional
    fun registerUser(user: UserCreate): User {
        // Check if Email exists
        if (userRepository.existsByEmail(user.email)) {
            throw IllegalArgumentException("Email already exists")
        }

        val hashedPassword = passwordEncoder.encode(user.password)
        val newUser = user.toUser().apply {
            password = hashedPassword
        }

        // Save the user to the database
        return userRepository.save(newUser)
    }

    /**
     * Logins a user
     * 
     * @param userLogin The login credentials
     * @return The logged in user
     */
    fun login(userLogin: UserLogin): User {
        val user = userRepository.findByEmail(userLogin.email)

        if (user == null) {
            // Timing attacks prevention
            passwordEncoder.matches(userLogin.password, userNotFoundEncodedPassword)
            throw IllegalArgumentException("Email or password incorrect")
        }

        if (!passwordEncoder.matches(userLogin.password, user.password)) {
            throw IllegalArgumentException("Email or password incorrect")
        }

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
            throw AccessDeniedException("User $currentUserId attempted to access user $id")
        }
        return userRepository.findById(id).orElseThrow { NoSuchElementException("User not found") }
    }

    /**
     * Updates a user
     * 
     * @param id The id of the user
     * @param currentUserId The id of the current user
     * @param userUpdate The user to update
     * @return The updated user
     */
    @Transactional
    fun updateUser(id: UUID, currentUserId: UUID, userUpdate: UserUpdate): User {
        // Check User have Permissions to update
        if (id != currentUserId) {
            throw AccessDeniedException("User $currentUserId attempted to update user $id")
        }
        val existingUser = userRepository.findById(id).orElseThrow { NoSuchElementException("User not found") }

        // Update only changed Attributes
        val updatedUser = userUpdate.toUser(existingUser)
        
        // Check if Email exists
        if (updatedUser.email != existingUser.email && userRepository.existsByEmail(updatedUser.email)) {
            throw IllegalArgumentException("Email already exists")
        }

        return userRepository.save(updatedUser)
    }

    /**
     * Updates a user's password
     * 
     * @param id The id of the user
     * @param currentUserId The id of the current user
     * @param passwordChange The password change DTO
     * @return The updated user
     */
    @Transactional
    fun updatePassword(id: UUID, passwordChange: UserPasswordUpdate, currentUserId: UUID): User {
        // Check User have Permissions to update
        if (id != currentUserId) {
            throw AccessDeniedException("User $currentUserId attempted to change password of user $id")
        }
        val existingUser = userRepository.findById(id).orElseThrow { NoSuchElementException("User not found") }

        // Check if Password is correct
        if (!passwordEncoder.matches(passwordChange.oldPassword, existingUser.password)) {
            throw IllegalArgumentException("Password is incorrect")
        }

        val hashedPassword = passwordEncoder.encode(passwordChange.newPassword)
        existingUser.password = hashedPassword

        return userRepository.save(existingUser)
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
            throw AccessDeniedException("User $currentUserId attempted to delete user $id")
        }
        userRepository.deleteById(id)
    }

}