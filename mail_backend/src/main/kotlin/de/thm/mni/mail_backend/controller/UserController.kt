package de.thm.mni.mail_backend.controller

import de.thm.mni.mail_backend.annotations.DefaultApiErrors
import de.thm.mni.mail_backend.dto.user.UserCreate
import de.thm.mni.mail_backend.dto.user.UserResponse
import de.thm.mni.mail_backend.services.UserService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@DefaultApiErrors
@RestController
@RequestMapping("/users")
class UserController (val userService: UserService) {
    @PostMapping
    fun registerUser(@Valid @RequestBody userCreate: UserCreate): UserResponse {
        val user = userService.registerUser(userCreate.toUser())
        
        return user.toResponse()
    }   
}