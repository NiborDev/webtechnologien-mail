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
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
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

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import de.thm.mni.mail_backend.dto.user.UserListResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestParam

@Tag(
    name = "User",
    description = "Manage user accounts including registration, authentication, profile updates, and deletion"
)
@DefaultApiErrors
@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService,
    val jwtService: JwtService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * POST /users Registers a new user
     *
     * @param userCreate The user to register
     * @return The registered user
     */
    @Operation(
        operationId = "registerUser",
        summary = "Register a new user",
        description = "Creates a new user account with the provided email, password, first name, and last name.",
        security = [SecurityRequirement(name = "")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "User registered successfully."),
            ApiResponse(responseCode = "400", description = "Validation error")
        ]
    )
    @SecurityRequirement(name = "")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun registerUser(@Valid @RequestBody userCreate: UserCreate): UserResponse {
        logger.info("Registering user with email: {}", userCreate.email)
        val user = userService.registerUser(userCreate)
        logger.info("User registered successfully with ID: {}", user.userId)
        return user.toResponse()
    }

    /**
     * POST /users/login Logs in a user
     *
     * @param userLogin The login credentials
     * @return The logged in user with JWT token
     */
    @Operation(
        operationId = "loginUser",
        summary = "Authenticate user",
        description = "Authenticates user credentials and returns a JWT bearer token alongside user profile data.",
        security = [SecurityRequirement(name = "")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully authenticated."),
            ApiResponse(responseCode = "401", description = "Invalid email or password.")
        ]
    )
    @SecurityRequirement(name = "")
    @PostMapping("/login")
    fun loginUser(@Valid @RequestBody userLogin: UserLogin): LoginResponse {
        logger.info("Login attempt for email: {}", userLogin.email)
        val user = userService.login(userLogin)
        val token = jwtService.createToken(user.userId.toString())
        logger.info("User logged in successfully with ID: {}", user.userId)
        return LoginResponse(token, user.toResponse())
    }


    /**
     * GET /users Returns all users with pagination and optional search filtering (email, firstName, lastName) without exposing user IDs
     *
     * @param search Optional search query string for filtering users
     * @param pageable Pageable Object for paging
     * @return A Page of user information
     */
    @Operation(
        operationId = "getAllUsers",
        summary = "Get paginated user list",
        description = "This function retrieves a list of users with optional search filtering by email, first name or last name."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Paginated list of users retrieved successfully.")
        ]
    )
    @GetMapping
    fun getAllUsers(
        @Parameter(description = "Optional search string to filter users by name or email")
        @RequestParam(required = false) search: String?,
        @ParameterObject pageable: Pageable
    ): Page<UserListResponse> {
        logger.debug("Retrieving all users with search filter: '{}'", search)
        val users = userService.getAllUsers(search, pageable)
        return users.map { it.toUserListResponse() }
    }

    /**
     * GET /users/{id} Returns a user by id
     *
     * @param id The id of the user
     * @param currentUser The current authenticated user
     * @return The user with the given id
     */
    @Operation(
        operationId = "getUserById",
        summary = "Get user by ID",
        description = "Retrieves user details by user ID. Users can only fetch their own profile details."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User profile details returned successfully."),
            ApiResponse(responseCode = "404", description = "User not found or access denied.")
        ]
    )
    @GetMapping("/{id}")
    fun getUserById(
        @Parameter(description = "UUID of the user") @PathVariable id: UUID,
        @AuthenticationPrincipal currentUser: UserDetails
    ): UserResponse {
        logger.debug("User {} requested profile for ID {}", currentUser.username, id)
        val user = userService.getUserById(id, UUID.fromString(currentUser.username))
        return user.toResponse()
    }

    /**
     * PUT /users/{id} Updates a user
     *
     * @param id The id of the user
     * @param userUpdate The user to update
     * @param currentUser The current authenticated user
     * @return The updated user
     */
    @Operation(
        operationId = "updateUser",
        summary = "Update user details",
        description = "Updates profile details (email, first name, last name) for the specified user."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User updated successfully."),
            ApiResponse(responseCode = "400", description = "Invalid request payload."),
            ApiResponse(responseCode = "404", description = "User not found or access denied.")
        ]
    )
    @PutMapping("/{id}")
    fun updateUser(
            @Parameter(description = "UUID of the user") @PathVariable id: UUID,
            @Valid @RequestBody userUpdate: UserUpdate,
            @AuthenticationPrincipal currentUser: UserDetails
    ): UserResponse {
        logger.info("User {} updating details for user ID {}", currentUser.username, id)
        val updatedUser = userService.updateUser(id, UUID.fromString(currentUser.username), userUpdate)
        logger.info("User ID {} updated successfully", id)
        return updatedUser.toResponse()
    }

    /**
     * PATCH /users/{id}/password Changes a user's password
     *
     * @param id The id of the user
     * @param passwordChange The password change DTO
     * @param currentUser The current authenticated user
     * @return The updated user
     */
    @Operation(
        operationId = "updatePassword",
        summary = "Change user password",
        description = "Updates the password for the specified user after validating the current password."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Password updated successfully."),
            ApiResponse(responseCode = "400", description = "Invalid current password or password validation failed."),
            ApiResponse(responseCode = "404", description = "User not found or access denied.")
        ]
    )
    @PatchMapping("/{id}/password")
    fun updatePassword(
            @Parameter(description = "UUID of the user") @PathVariable id: UUID,
            @Valid @RequestBody passwordChange: UserPasswordUpdate,
            @AuthenticationPrincipal currentUser: UserDetails
    ): UserResponse {
        logger.info("User {} changing password for user ID {}", currentUser.username, id)
        val updatedUser = userService.updatePassword(id, passwordChange, UUID.fromString(currentUser.username))
        logger.info("Password updated successfully for user ID {}", id)
        return updatedUser.toResponse()
    }

    /**
     * DELETE /users/{id} Deletes a user
     *
     * @param id The id of the user
     * @param currentUser The current authenticated user
     */
    @Operation(
        operationId = "deleteUser",
        summary = "Delete user account",
        description = "Deletes the user account with the specified ID."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "User deleted successfully."),
            ApiResponse(responseCode = "404", description = "User not found or access denied.")
        ]
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@Parameter(description = "UUID of the user") @PathVariable id: UUID, @AuthenticationPrincipal currentUser: UserDetails) {
        logger.info("User {} deleting account for user ID {}", currentUser.username, id)
        userService.deleteUser(id, UUID.fromString(currentUser.username))
        logger.info("User ID {} deleted successfully", id)
    }
}
