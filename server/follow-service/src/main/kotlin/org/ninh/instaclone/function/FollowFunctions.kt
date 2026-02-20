package org.ninh.instaclone.function

import org.ninh.instaclone.dto.FollowRequest
import org.ninh.instaclone.dto.HandleFollowRequest
import org.ninh.instaclone.service.FollowService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Function

@Configuration
class FollowFunctions(
    private val followService: FollowService
) {
    @Bean
    fun createFollowRequest() : Function<FollowRequest, String> = Function { request ->
        try {
            followService.createFollowRequest(request)
            "Follow request created"
        } catch (e: Exception){
            e.printStackTrace()
            throw RuntimeException("Follow request creation failed: " + e.message)
        }
    }

    @Bean
    fun handleFollowRequest() : Function<HandleFollowRequest, String> = Function { request ->
        try {
            followService.handleFollowRequest(request)
            "Handle follow request successful"
        } catch (e: Exception){
            e.printStackTrace()
            throw RuntimeException("Handle follow request failed: " + e.message)
        }
    }
}