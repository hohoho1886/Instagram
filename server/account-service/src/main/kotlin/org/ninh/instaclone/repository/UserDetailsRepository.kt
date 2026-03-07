package org.ninh.instaclone.repository

import jakarta.transaction.Transactional
import org.ninh.instaclone.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface UserDetailsRepository : JpaRepository<User, String> {
    fun findByUsername(username: String): Optional<User>
    fun existsByUsername(username: String): Boolean

    @Modifying
    @Transactional
    @Query(
        """
        UPDATE "user_account"
        SET
            followers_count = GREATEST(
                followers_count + CASE WHEN user_id = :followeeId THEN :delta ELSE 0 END,
                0
            ),
            following_count = GREATEST(
                following_count + CASE WHEN user_id = :followerId THEN :delta ELSE 0 END,
                0
            )
        WHERE user_id IN (:followeeId, :followerId)
    """,
        nativeQuery = true
    )
    fun batchUpdateFollowCounters(
        @Param("followeeId") followeeId: UUID,
        @Param("followerId") followerId: UUID,
        @Param("delta") delta: Int
    ): Int

    @Query("SELECT u.followersCount FROM User u WHERE u.userId = :userId")
    fun getFollowersCount(userId: UUID): Int
}