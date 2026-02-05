package org.ninh.instaclone.function


import org.ninh.instaclone.dto.RegisterUserRequest
import org.ninh.instaclone.dto.UserResponse
import org.ninh.instaclone.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Function

@Configuration
class UserFunctions(val userService: UserService) {
    @Bean
    fun registerUser(): Function<RegisterUserRequest, UserResponse> {
        return Function { request ->
            try {
                userService.registerUser(request)
            } catch (ex: Exception) {
                throw RuntimeException("Registration failed: ${ex.message}", ex)
            }
        }
    }
}
