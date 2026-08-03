package de.thm.mni.mail_backend.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Filter for authenticating requests with JWT
 * It checks for a valid JWT token in the Authorization header.
 * If a valid JWT token is found, it creates an authentication in the SecurityContextHolder.
 * 
 * @param jwtService Service for JWT operations
 */
@Component
class JwtAuthFilter(private val jwtService: JwtService) : OncePerRequestFilter() {
    override fun doFilterInternal(
        req: HttpServletRequest,
        res: HttpServletResponse,
        filterChain: FilterChain
    ) {
        JWTAuthenticationFactory.create(req, jwtService)?.let {
            SecurityContextHolder.getContext().authentication = it
        }
        filterChain.doFilter(req, res)
    }
}