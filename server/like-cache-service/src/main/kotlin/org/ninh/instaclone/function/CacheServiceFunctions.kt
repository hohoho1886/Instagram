package org.ninh.instaclone.function

import org.ninh.instaclone.service.CacheService
import org.springframework.context.annotation.Configuration
import java.util.function.Function

data class LikeDto(
    val postId: String,
    val userId: String
)

@Configuration
class CacheServiceFunctions(
    private val cacheService: CacheService
) {
    fun incrementLike(): Function<LikeDto, String> = Function { likeDto ->
        cacheService.incrementLike(likeDto)
        "Like processed"
    }

}
