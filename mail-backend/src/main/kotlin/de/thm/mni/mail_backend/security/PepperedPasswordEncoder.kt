package de.thm.mni.mail_backend.security

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

class PepperedPasswordEncoder(
    private val pepper: String,
    private val saltLength: Int = 32,
    private val hashLength: Int = 32,
    private val parallelism: Int = 1,
    private val memoryAsKB: Int = 1 shl 16,
    private val iterations: Int = 6,
): PasswordEncoder {

    init {
    // ensure valid parameters }
    }
         private val argon2 = Argon2PasswordEncoder(
            saltLength, hashLength, parallelism, memoryAsKB, iterations
        )

        override fun encode(rawPassword: CharSequence?): String? {
            return argon2.encode(rawPassword.toString() + pepper)
        }

        override fun matches(
            rawPassword: CharSequence?, encodedPassword: String?
        ): Boolean {
            return argon2.matches(rawPassword.toString() + pepper, encodedPassword)
        }
}