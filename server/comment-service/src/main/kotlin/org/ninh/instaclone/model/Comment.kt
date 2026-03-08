package org.ninh.instaclone.model

import jakarta.persistence.Column
import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import java.util.UUID
import kotlin.time.Clock
import kotlin.time.Instant

@PrimaryKeyClass
data class CommentKey(
    @PrimaryKeyColumn(
        name = "post_id",
        ordinal = 0,
        type = PrimaryKeyType.PARTITIONED
    )
    val postId: UUID,

    @PrimaryKeyColumn(
        name = "created_at",
        ordinal = 1,
        type = PrimaryKeyType.CLUSTERED
    )
    val createdAt: Instant = Clock.System.now(),

    @PrimaryKeyColumn(
        name = "comment_id",
        ordinal = 2,
        type = PrimaryKeyType.CLUSTERED
    )
    val commentId: UUID = UUID.randomUUID()
)

@Table("comments")
data class Comment(
    @PrimaryKey
    val key: CommentKey,

    @Column("author_id")
    val authorId: UUID,

    @Column("parent_comment_id")
    val parentCommentId: UUID? = null,

    @Column("content")
    val content: String,

    @Column("likes_count")
    val likesCount: Long = 0,
)