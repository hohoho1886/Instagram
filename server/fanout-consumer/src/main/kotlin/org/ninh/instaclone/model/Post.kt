package org.ninh.instaclone.model

import org.springframework.data.cassandra.core.cql.Ordering
import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import java.util.UUID

@PrimaryKeyClass
data class PostKey(
    @PrimaryKeyColumn(name = "receiver_userId", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    val receiverUserId: String,

    @PrimaryKeyColumn(name = "created_at", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    val createdAt: String,

    @PrimaryKeyColumn(name = "post_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    val postId: UUID
)

@Table(value  = "posts", keyspace = "posts_write")
data class Post (
    @PrimaryKey
    val key: PostKey,
    @Column("media_urls")
    val mediaUrls: String,
    @Column("likes_count")
    val likesCount: Long,
    @Column("comments_count")
    val commentsCount: Long,
)