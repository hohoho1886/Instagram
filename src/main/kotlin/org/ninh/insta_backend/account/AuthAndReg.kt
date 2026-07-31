package org.ninh.insta_backend.account

import org.ninh.insta_backend.account.dto.AuthRequest
import org.ninh.insta_backend.account.dto.AuthResponse
import org.ninh.insta_backend.account.dto.RegisterRequest
import org.ninh.insta_backend.account.dto.RegisterResponse
import org.ninh.insta_backend.account.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthAndReg(
    private val userService: UserService
) {
    @PostMapping("/register")
    fun registerUser(@RequestBody request: RegisterRequest): ResponseEntity<RegisterResponse>{
        return try {
            val res = userService.registerUser(request)
            ResponseEntity.ok(res)
        } catch (ex: Exception) {
            ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(RegisterResponse(error = "Registration failed: ${ex.message}"))
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest): ResponseEntity<AuthResponse> {
        return try {
            val response = userService.loginUser(request)
            ResponseEntity.ok(response)
        } catch (ex: Exception) {
            ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(AuthResponse(error = "Login failed: ${ex.message}"))
        }
    }
}

