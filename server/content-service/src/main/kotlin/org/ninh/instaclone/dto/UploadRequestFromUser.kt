package org.ninh.instaclone.dto

import jakarta.validation.constraints.NotBlank

data class UploadRequestFromUser(
    @field:NotBlank(message = "{req.postId.notBlank}")
    val postId: String,
    @field:NotBlank(message = "{req.userId.notBlank}")
    val authorId: String,
    @field:NotBlank(message = "{req.mediaUrl.notBlank}")
    val mediaUrls: String
)