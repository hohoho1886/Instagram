package org.ninh.instaclone.repository

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID


@Embeddable
data class FollowIdMirror(
    @Column(name = "follower_id")
    val followerId: UUID
)

@Entity
@Table(name = "follows")
data class FollowMirror(
    @EmbeddedId
    val id: FollowIdMirror
)

interface FollowRepository: JpaRepository<FollowMirror, FollowIdMirror> {
    @Query("SELECT f.follower_id FROM follows f WHERE f.followee_id = :followee_id AND f.status = 'ACCEPTED'", nativeQuery = true)
    fun findAcceptedFollowers(@Param("followee_id") followeeId: UUID): List<UUID>
}