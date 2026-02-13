package org.ninh.instaclone.dto

data class PostMessage(
    val postId: String,
    val userId: String,
    val mediaUrls: String,
    val createdAt: String?,
    val likesCount: Long,
    val commentsCount: Long
)
