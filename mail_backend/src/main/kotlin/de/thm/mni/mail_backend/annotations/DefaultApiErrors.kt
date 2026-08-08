package de.thm.mni.mail_backend.annotations

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.MediaType

/**
 * Annotation for default API error responses.
 * Adds the following error responses to a controller:
 * - 400: Bad Request - Validation failed or invalid argument.
 * - 401: Unauthorized - Authentication required.
 * - 403: Forbidden - Access denied.
 * - 404: Not Found - Resource not found.
 * - 500: Internal Server Error.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponse(responseCode = "400", description = "Bad Request - Validation failed or invalid argument.")
@ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required.")
@ApiResponse(responseCode = "403", description = "Forbidden - Access denied.")
@ApiResponse(responseCode = "404", description = "Not Found - Resource not found.")
@ApiResponse(responseCode = "500", description = "Internal Server Error.", content = [
    Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = Schema(implementation = Error::class)
    )
])
annotation class DefaultApiErrors ()