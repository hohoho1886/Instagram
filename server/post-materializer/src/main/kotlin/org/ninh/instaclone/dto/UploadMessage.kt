package org.ninh.instaclone.dto

data class UploadMessage(
    val postId: String,
    val userId: String,
    val mediaUrls: String,
    val timestamp: Long = System.currentTimeMillis()
)