package org.ninh.instaclone.function

import java.util.function.Function
import org.mindrot.jbcrypt.BCrypt
import org.ninh.instaclone.dto.AuthRequest
import org.ninh.instaclone.dto.AuthResponse
import org.ninh.instaclone.dto.RegisterRequest
import org.ninh.instaclone.dto.RegisterResponse
import org.ninh.instaclone.message.Messages
import org.ninh.instaclone.service.UserService
import org.ninh.instaclone.utils.JwtUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated

@Validated
@Configuration
class AuthentificationAndRegistration(
    private val userService: UserService,
    private val jwtUtils: JwtUtils,
    private val messages: Messages
) {
  @Bean
  fun registerUser(): Function<RegisterRequest, RegisterResponse> {
    return Function { request ->
      try {
        userService.registerUser(request)
      } catch (ex: Exception) {
        throw RuntimeException("Registration failed: ${ex.message}", ex)
      }
    }
  }

  @Bean
  fun login(): Function<AuthRequest, AuthResponse> = Function { request ->
    try {
      val userOptional = userService.findByUsername(request.username)

      val user = userOptional.get()
      val passwordMatches = BCrypt.checkpw(request.password, user.passwordHash)
      if (!passwordMatches) {
        return@Function AuthResponse(error = messages.invalidCredentials())
      }
      val token = jwtUtils.generateToken(user.username)
      AuthResponse(jwtToken = token)
    } catch (_: NoSuchElementException){
      return@Function AuthResponse(error = messages.invalidCredentials())
    } catch (ex: Exception) {
      ex.printStackTrace()
      return@Function AuthResponse(error = messages.unexpectedErr())
    }
  }
}
