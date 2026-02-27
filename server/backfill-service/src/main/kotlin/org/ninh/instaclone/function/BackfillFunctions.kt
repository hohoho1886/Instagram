package org.ninh.instaclone.function

import org.ninh.instaclone.service.BackfillService
import org.ninh.instaclone.service.PostId
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Supplier

@Configuration
class BackfillFunctions(
    private val backfillService: BackfillService
) {
    @Bean
    fun testBackfill(): Supplier<List<PostId>> = Supplier {
        backfillService.getPostsForFollowedUsers(listOf("user_67890"))
    }
}