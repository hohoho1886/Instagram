package org.ninh.instaclone.dto

import org.ninh.instaclone.model.FollowStatus

data class FollowRequest(
    val followeeUsername: String,
    val followerUsername: String
)

data class HandleFollowRequest(
    val followeeUsername: String,
    val followerUsername: String,
    val status: FollowStatus
)