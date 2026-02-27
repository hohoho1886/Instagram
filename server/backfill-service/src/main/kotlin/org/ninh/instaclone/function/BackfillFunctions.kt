package org.ninh.instaclone.function

import org.ninh.instaclone.repository.FollowRepository
import org.ninh.instaclone.service.BackfillService
import org.ninh.instaclone.service.NewPosts
import org.ninh.instaclone.service.PostId
import org.ninh.instaclone.service.Username
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Supplier
import java.util.function.Consumer
import java.util.function.Function

@Configuration
class BackfillFunctions(
    private val backfillService: BackfillService,
    private val followRepository: FollowRepository
) {
    @Bean
    fun testBackfill(): Supplier<List<PostId>> = Supplier {
        backfillService.getPostsForFollowedUsers(listOf("user_67890"))
    }
    /*
    @Bean
    fun activate(): Function<Username, String> = Function { username ->
        val followedUsers = followRepository.findFollowees(username)
        val newPosts = backfillService.getPostsForFollowedUsers(followedUsers)
        val newPostsDto = NewPosts(
            userUsername = username,
            posts = newPosts
        )
        backfillService.publish(newPostsDto)
        "Message published successfully"
    }

     */
}