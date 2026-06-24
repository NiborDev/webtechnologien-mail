package de.thm.mni.mail_backend.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(@Value("\${app.secret}") private val pepper: String) {
    @Bean
    fun passwordEncoder(): PasswordEncoder = PepperedPasswordEncoder(pepper)

    @Bean
    fun securityFilterChain(http: HttpSecurity, authFilter: JwtAuthFilter): SecurityFilterChain = http
        .csrf { it.disable() } // Explicitly disable CSRF protection for stateless APIs
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .authorizeHttpRequests {
            it.requestMatchers("/register").permitAll()
            it.requestMatchers("/login").permitAll()
            it.requestMatchers("/v3/api-docs/**").permitAll()
            it.requestMatchers("/swagger-ui/**").permitAll()
            it.requestMatchers("/logout").permitAll()
            it.anyRequest().authenticated() // Protect all other endpoints
        }
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter::class.java)
        .build()
}
