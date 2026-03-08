package org.ninh.instaclone.model

import org.springframework.data.cassandra.core.cql.PrimaryKeyType
import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn
import org.springframework.data.cassandra.core.mapping.Table
import java.util.UUID
import java.time.Instant

@PrimaryKeyClass
data class LikeKey(
    @PrimaryKeyColumn(
        name = "post_id",
        ordinal = 0,
        type = PrimaryKeyType.PARTITIONED
    )
    val postId: UUID,
    @PrimaryKeyColumn(
        name = "user_id",
        ordinal = 1,
        type = PrimaryKeyType.CLUSTERED
    )
    val userId: UUID
)

@Table("likes")
data class Like(
    @PrimaryKey
    val key: LikeKey,
    @Column("created_at")
    val created_at: Instant = Instant.now(),
)