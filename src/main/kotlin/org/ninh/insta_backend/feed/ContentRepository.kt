package org.ninh.insta_backend.feed

import org.ninh.insta_backend.feed.model.FeedPostProjection
import org.ninh.insta_backend.post.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface ContentRepository: JpaRepository<Post, Long> {
    @Query(
        nativeQuery = true,
        value = """
        SELECT *
        FROM (
            SELECT DISTINCT ON (p.author_id)
                p."id" AS post_id,
                p.author_id,
                p.caption,
                p.media_url,
                p.created_at,
                (
                    SELECT COUNT(*) 
                    FROM "comments" c 
                    WHERE c.post_id = p."id"
                ) AS comment_count,
                (
                    SELECT COUNT(*)
                    FROM "likes" l
                    WHERE l.post_id = p."id"
                ) AS like_count
            FROM "posts" p
            JOIN "follows" f ON p.author_id = f.following_id
            WHERE f.follower_id = :currentUserId
            ORDER BY p.author_id, p.created_at DESC
        ) latest_posts
        ORDER BY created_at DESC
        LIMIT :limit
        """
    )
    fun getLatestFeedForUser(
        @Param("currentUserId") currentUserId: UUID,
        @Param("limit") limit: Int = 10
    ): List<FeedPostProjection>
}
