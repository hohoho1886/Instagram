package org.ninh.instaclone.repository

import jakarta.transaction.Transactional
import org.ninh.instaclone.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

interface UserDetailsRepository : JpaRepository<User, String> {
    fun findByUsername(username: String): Optional<User>
    fun existsByUsername(username: String): Boolean
    @Modifying
    @Transactional
    @Query("""
    UPDATE "user_account"
    SET 
        followers_count = 
            CASE 
                WHEN :counterType = 'FOLLOWERS' AND :action = 'INCREMENT' 
                THEN followers_count + 1
                WHEN :counterType = 'FOLLOWERS' AND :action = 'DECREMENT' AND followers_count > 0
                THEN followers_count - 1
                ELSE followers_count
            END,
        following_count = 
            CASE 
                WHEN :counterType = 'FOLLOWING' AND :action = 'INCREMENT'
                THEN following_count + 1
                WHEN :counterType = 'FOLLOWING' AND :action = 'DECREMENT' AND following_count > 0
                THEN following_count - 1
                ELSE following_count
            END
    WHERE username = :username
""",
        nativeQuery = true
    )
    fun updateFollowCounter(
        @Param("username") username: String,
        @Param("counterType") counterType: String,
        @Param("action") action: String
    ): Int
}