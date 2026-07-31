package org.ninh.insta_backend.account.dto

import java.util.UUID

data class RegisterRequest(
    val username: String,
    val password: String
)

data class RegisterResponse(
    val userId: UUID? = null,
    val username: String? = null,
    val error: String? = null
)

data class AuthRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val jwtToken: String? = null,
    val error: String? = null
)
