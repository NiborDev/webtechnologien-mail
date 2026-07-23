package de.thm.mni.mail_backend.controller

import de.thm.mni.mail_backend.annotations.DefaultApiErrors
import de.thm.mni.mail_backend.dto.user.UserPasswordUpdate
import de.thm.mni.mail_backend.dto.user.UserCreate
import de.thm.mni.mail_backend.dto.user.UserLogin
import de.thm.mni.mail_backend.dto.user.LoginResponse
import de.thm.mni.mail_backend.dto.user.UserResponse
import de.thm.mni.mail_backend.dto.user.UserUpdate
import de.thm.mni.mail_backend.security.JwtService
import de.thm.mni.mail_backend.services.UserService
import jakarta.validation.Valid
import java.util.UUID
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@DefaultApiErrors
@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService,
    val jwtService: JwtService
) {

    /**
     * POST /users Registers a new user
     *
     * @param userCreate The user to register
     * @return The registered user
     */
    @PostMapping
    fun registerUser(@Valid @RequestBody userCreate: UserCreate): UserResponse {
        val user = userService.registerUser(userCreate.toUser())

        return user.toResponse()
    }

    /**
     * POST /users/login Logins a user
     *
     * @param userLogin The login credentials
     * @return The logged in user with JWT token
     */
    @PostMapping("/login")
    fun loginUser(@Valid @RequestBody userLogin: UserLogin): LoginResponse {
        val user = userService.login(userLogin)
        val token = jwtService.createToken(user.email)

        return LoginResponse(token, user.toResponse())
    }


    /**
     * GET /users Returns all users
     *
     * @return A list of all users
     */
    @GetMapping
    fun getAllUsers(): List<UserResponse> {
        val users = userService.getAllUsers()
        return users.map { it.toResponse() }
    }

    /**
     * GET /users/{id} Returns a user by id
     *
     * @param id The id of the user
     * @return The user with the given id
     */
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: UUID): UserResponse {
        val user = userService.getUserById(id)
        return user.toResponse()
    }

    /**
     * PUT /users/{id} Updates a user
     *
     * @param id The id of the user
     * @param user The user to update
     * @return The updated user
     */
    @PutMapping("/{id}")
    fun updateUser(
            @PathVariable id: UUID,
            @Valid @RequestBody userUpdate: UserUpdate
    ): UserResponse {
        val updatedUser = userService.updateUser(id, userUpdate)
        return updatedUser.toResponse()
    }

    /**
     * PATCH /users/{id}/password Changes a user's password
     *
     * @param id The id of the user
     * @param passwordChange The password change DTO
     * @return The updated user
     */
    @PatchMapping("/{id}/password")
    fun updatePassword(
            @PathVariable id: UUID,
            @Valid @RequestBody passwordChange: UserPasswordUpdate
    ): UserResponse {
        val updatedUser = userService.updatePassword(id, passwordChange)
        return updatedUser.toResponse()
    }

    /**
     * DELETE /users/{id} Deletes a user
     *
     * @param id The id of the user
     */
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: UUID) {
        return userService.deleteUser(id)
    }
}
