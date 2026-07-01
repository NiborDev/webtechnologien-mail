package de.thm.mni.mail_backend.security

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource

const val AUTHORIZATION_HEADER = "Authorization"
const val BEARER_PREFIX = "Bearer "
const val ROLES_CLAIM = "roles"

object JWTAuthenticationFactory {
    fun create(request: HttpServletRequest, jwtService: JwtService): Authentication? {
        val token = request.getHeader(AUTHORIZATION_HEADER)
            ?.takeIf { it.startsWith(BEARER_PREFIX) } // Turns null if header is missing or doesn't start with "Bearer "
            ?.removePrefix(BEARER_PREFIX)
            ?: return null // No token found, return null to indicate no authentication
        val jwt = jwtService.validate(token) ?: return null
        val roles = jwt.getClaim(ROLES_CLAIM)?.asArray(String::class.java) ?: arrayOf("USER")
        val userDetails = User.builder()
            .username(jwt.subject)
            .password("") // No password needed for JWT, but required, so we set an empty string
            .roles(*roles)
            .build()
        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities).apply {
            details = WebAuthenticationDetailsSource().buildDetails(request)
        }
    }
}