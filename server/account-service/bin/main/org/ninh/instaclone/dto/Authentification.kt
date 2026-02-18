package org.ninh.instaclone.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

data class RegisterRequest(
    @field:NotBlank(message = "{req.username.notblank}")
    @field:Size(min = 3, max = 30)
    val username: String,

    @field:NotBlank(message = "{req.password.notblank}")
    @field:Size(min = 6)
    val password: String,

    @field:NotBlank(message = "{req.email.notblank}")
    val email: String
)

data class RegisterResponse(
    val userId: UUID? = null,
    val username: String? = null,
    val error: String? = null
)

data class AuthRequest(
    @field:NotBlank(message = "{req.username.notblank}")
    val username: String,

    @field:NotBlank(message = "{req.password.notblank}")
    val password: String
)

data class AuthResponse(
    val jwtToken: String? = null,
    val error: String? = null
)
