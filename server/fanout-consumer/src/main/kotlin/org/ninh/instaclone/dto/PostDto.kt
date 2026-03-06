package org.ninh.instaclone.dto


data class PostDto(
    val postId: String,
    val createdAt: String,
    val mediaUrl: String,
    val likesCount: Long,
    val commentsCount: Long
)