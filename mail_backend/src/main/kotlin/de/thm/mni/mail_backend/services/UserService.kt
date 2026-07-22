package de.thm.mni.mail_backend.services

import de.thm.mni.mail_backend.data.User
import de.thm.mni.mail_backend.dto.user.UserLogin
import de.thm.mni.mail_backend.dto.user.UserUpdate
import de.thm.mni.mail_backend.dto.user.UserPasswordUpdate
import de.thm.mni.mail_backend.repositories.interfaces.UserRepository
import de.thm.mni.mail_backend.security.PepperedPasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

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
    fun registerUser(user: User): User {
        // Check if Email exists
        if (userRepository.existsByEmail(user.email)) {
            throw IllegalArgumentException("Email already exists")
        }

        val hashedPassword = passwordEncoder.encode(user.password)
        val userToSave = user.copy(password = hashedPassword)

        // Save the user to the database
        return userRepository.save(userToSave)
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
     * Returns all Users
     * 
     * @return A list of all users
     */
    fun getAllUsers(): List<User> {
        return userRepository.findAll().toList()
    }

    /**
     * Returns a user by id
     * 
     * @param id The id of the user
     * @return The user with the given id
     */
    fun getUserById(id: UUID): User {
        return userRepository.findById(id).orElseThrow { NoSuchElementException("User not found") }
    }

    /**
     * Updates a user
     * 
     * @param id The id of the user
     * @param userUpdate The user to update
     * @return The updated user
     */
    fun updateUser(id: UUID, userUpdate: UserUpdate): User {
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
     * @param passwordChange The password change DTO
     * @return The updated user
     */
    fun updatePassword(id: UUID, passwordChange: UserPasswordUpdate): User {
        val existingUser = userRepository.findById(id).orElseThrow { NoSuchElementException("User not found") }

        // Check if Password is correct
        if (!passwordEncoder.matches(passwordChange.oldPassword, existingUser.password)) {
            throw IllegalArgumentException("Password is incorrect")
        }

        val hashedPassword = passwordEncoder.encode(passwordChange.newPassword)
        val updatedUser = existingUser.copy(password = hashedPassword)

        return userRepository.save(updatedUser)
    }

    /**
     * Deletes a user
     * 
     * @param id The id of the user
     */
    fun deleteUser(id: UUID) {
        userRepository.deleteById(id)
    }

}