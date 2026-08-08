package de.thm.mni.mail_backend.controller.validation

import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Advice for handling validation errors.
 * Catches [MethodArgumentNotValidException] and returns a response with the validation errors.
 */
@RestControllerAdvice
class ValidationErrorAdvice {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleErrors(ex: MethodArgumentNotValidException): ResponseEntity<MutableMap<String, String>> {
        val response: MutableMap<String, String> = LinkedHashMap()
        ex.bindingResult.fieldErrors.forEach { error: FieldError -> response[error.field] = error.defaultMessage ?: "" }
        return ResponseEntity.badRequest().body(response)
    }
}