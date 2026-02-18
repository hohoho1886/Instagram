package org.ninh.instaclone.function

import java.util.function.Function
import org.ninh.instaclone.dto.AuthRequest
import org.ninh.instaclone.dto.AuthResponse
import org.ninh.instaclone.dto.RegisterRequest
import org.ninh.instaclone.dto.RegisterResponse
import org.ninh.instaclone.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated

@Validated
@Configuration
class AuthentificationAndRegistration(
    private val userService: UserService
) {
  @Bean
  fun registerUser(): Function<RegisterRequest, RegisterResponse> {
    return Function { request ->
      try {
        userService.registerUser(request)
      } catch (ex: Exception) {
        RegisterResponse(error = "Registration failed: ${ex.message}")
      }
    }
  }

  @Bean
  fun login(): Function<AuthRequest, AuthResponse> = Function { request ->
    try {
      userService.loginUser(request)
    } catch (ex: Exception) {
      ex.printStackTrace()
      AuthResponse(error = "Registration failed: ${ex.message}")
    }
  }
}
