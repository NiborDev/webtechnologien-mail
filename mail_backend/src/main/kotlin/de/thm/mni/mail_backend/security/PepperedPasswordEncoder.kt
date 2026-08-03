package de.thm.mni.mail_backend.security

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Password encoder that uses Argon2 with a pepper.
 *
 * @param pepper The pepper to use for encoding and matching
 * @param saltLength The length of the salt
 * @param hashLength The length of the hash
 * @param parallelism The parallelism to use for encoding and matching
 * @param memoryAsKB The memory to use for encoding and matching
 * @param iterations The number of iterations to use for encoding and matching
 */
class PepperedPasswordEncoder(
        private val pepper: String,
        private val saltLength: Int = 32,
        private val hashLength: Int = 32,
        private val parallelism: Int = 1,
        private val memoryAsKB: Int = 1 shl 16,
        private val iterations: Int = 6,
) : PasswordEncoder {

    init {
        // ensure valid parameters
    }

    /** The Argon2PasswordEncoder instance used for encoding and matching */
    private val argon2 =
            Argon2PasswordEncoder(saltLength, hashLength, parallelism, memoryAsKB, iterations)

    /**
     * Encodes a password with pepper
     * @param rawPassword The password to encode
     * @return The encoded password with pepper
     */
    override fun encode(rawPassword: CharSequence?): String {
        return requireNotNull(argon2.encode(rawPassword.toString() + pepper)) {
            "Argon2 encoding failed"
        }
    }

    /**
     * Matches a password with the encoded password
     * @param rawPassword The raw password to match
     * @param encodedPassword The encoded password to match
     * @return True if the raw password matches the encoded password.
     */
    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
        if (encodedPassword == null) return false
        return argon2.matches(rawPassword.toString() + pepper, encodedPassword)
    }
}
