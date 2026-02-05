package org.ninh.instaclone.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

data class RegisterUserRequest(
    @field:NotBlank(message = "Username cannot be empty")
    @field:Size(min = 3, max = 30)
    val username: String,

    @field:NotBlank(message = "Password cannot be empty")
    @field:Size(min = 6)
    val password: String,

    @field:NotBlank(message = "Email cannot be empty")
    val email: String
)

data class UserResponse(
    val userId: UUID,
    val username: String
)
