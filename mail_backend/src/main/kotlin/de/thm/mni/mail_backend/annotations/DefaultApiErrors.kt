package de.thm.mni.mail_backend.annotations

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.MediaType


@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ApiResponse(responseCode = "401", description = "Unauthorized.")
@ApiResponse(responseCode = "500", description = "Internal server Error.", content = [
    Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        schema = Schema(implementation = Error::class)
    )
])
annotation class DefaultApiErrors ()