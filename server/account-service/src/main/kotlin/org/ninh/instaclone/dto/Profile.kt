package org.ninh.instaclone.dto

data class Profile (
    val username: String,
    val profilePictureUrl: String?,
    val followersCount: Int,
    val followingCount: Int
)