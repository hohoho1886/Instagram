package org.ninh.instaclone.function

import org.ninh.instaclone.dto.Profile
import org.ninh.instaclone.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Function

data class FetchAccountDetailRequest(
    val username: String
)

@Configuration
class AccountFunctions (
    private val userService: UserService
){
    @Bean
    fun fetchAccountDetail(): Function<FetchAccountDetailRequest, Profile?> = Function { rq ->
        userService.findByUsername(rq.username)
    }
}