package de.thm.mni.mail_backend.openapi

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configures the OpenAPI Authentification
 * Default: All routes are secured by JWT token.
 * For routes that don't need authentification they must annotate wit a custom @SecurityRequirement annotation.
 *
 * This follows the example on: https://www.baeldung.com/openapi-jwt-authentication#bd-3-global-configuration
 */
@Configuration
class OpenApiConfiguration {
    @Bean
    fun customizeOpenAPI(): OpenAPI? {
        val securitySchemeName = "bearerAuth"
        return OpenAPI()
            .addSecurityItem(
                SecurityRequirement()
                    .addList(securitySchemeName)
            )
            .components(
                Components()
                    .addSecuritySchemes(
                        securitySchemeName, SecurityScheme()
                            .name(securitySchemeName)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            )
    }
}