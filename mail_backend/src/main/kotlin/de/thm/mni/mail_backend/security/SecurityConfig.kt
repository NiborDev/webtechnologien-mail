package de.thm.mni.mail_backend.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(@Value("\${app.secret}") private val pepper: String) {
    @Bean
    fun passwordEncoder(): PepperedPasswordEncoder = PepperedPasswordEncoder(pepper)

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            allowedOrigins = listOf(
                "http://localhost:4200",
                "http://127.0.0.1:4200",
                "http://localhost",
                "http://localhost:80"
            )
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            allowedHeaders = listOf("*")
            allowCredentials = true
            maxAge = 3600L
        }
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity, authFilter: JwtAuthFilter): SecurityFilterChain = http
        .cors { it.configurationSource(corsConfigurationSource()) }
        .csrf { it.disable() } // Explicitly disable CSRF protection for stateless APIs
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .authorizeHttpRequests {
            it.requestMatchers("/users").permitAll()
            it.requestMatchers("/users/login").permitAll()
            it.requestMatchers("/v3/api-docs/**").permitAll()
            it.requestMatchers("/swagger-ui/**").permitAll()
            it.requestMatchers("/logout").permitAll()
            it.anyRequest().authenticated() // Protect all other endpoints
        }
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter::class.java)
        .build()
}
