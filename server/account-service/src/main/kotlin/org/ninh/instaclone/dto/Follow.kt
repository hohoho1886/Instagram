package org.ninh.instaclone.dto

data class FollowCounterEvent(
    val followeeId: String,
    val followerId: String,
    val type: FollowCounterType
)

enum class FollowCounterType {
    INCREMENT,
    DECREMENT
}