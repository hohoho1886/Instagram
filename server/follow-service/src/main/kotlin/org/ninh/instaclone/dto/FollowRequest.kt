package org.ninh.instaclone.dto

import org.ninh.instaclone.model.FollowStatus

data class FollowRequest(
    val followeeId: String,
    val followerId: String
)

data class HandleFollowRequest(
    val followeeId: String,
    val followerId: String,
    val status: FollowStatus
)