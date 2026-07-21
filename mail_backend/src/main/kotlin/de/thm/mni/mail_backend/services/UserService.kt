package de.thm.mni.mail_backend.services

import de.thm.mni.mail_backend.data.User
import de.thm.mni.mail_backend.repositories.UserRepository
import de.thm.mni.mail_backend.security.PepperedPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    fun registerUser(user: User): User {
        // Check if Email exists
        if (userRepository.existsByEmail(user.email)) {
            throw IllegalArgumentException("Email already exists")
        }

        val hashedPassword = PepperedPasswordEncoder("somepepper").encode(user.password)
        var user = user.copy(password = hashedPassword.toString())

        // Save the user to the database
        return userRepository.save(user)
    }
}