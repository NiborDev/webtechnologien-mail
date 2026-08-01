package de.thm.mni.mail_backend.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Date

/**
 * Service for JWT operations.
 * 
 * @param secret The secret key used for signing and verifying JWTs.
 * @param appName The name of the application used as the issuer.
 * @param expiresSeconds The time in seconds until a JWT expires.
 */
@Service
class JwtService(
    @Value("\${app.jwt.secret}") private val secret: String,
    @Value("\${app.name}") private val appName: String,
    @Value("\${app.jwt.expires-seconds}") private val expiresSeconds: Long,
) {
    private val verifier = JWT
        .require(Algorithm.HMAC256(secret))
        .withIssuer(appName)
        .build()

    /**
     * Creates a JWT token.
     * 
     * @param subject The subject of the token.
     * @return JWT token.
     */
    fun createToken(subject: String): String {
        val now = Instant.now()
        val exp = now.plusSeconds(expiresSeconds)
        return JWT.create()
            .withIssuer(appName)
            .withSubject(subject)
            .withIssuedAt(Date.from(now))
            .withExpiresAt(Date.from(exp))
            .sign(Algorithm.HMAC256(secret))
    }

    /**
     * Validates a JWT token.
     * 
     * @param token The JWT token to validate.
     * @return Decoded JWT token.
     */
    fun validate(token: String): DecodedJWT = verifier.verify(token)
}