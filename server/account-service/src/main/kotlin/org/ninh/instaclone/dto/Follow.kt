package org.ninh.instaclone.dto

data class FollowCounterEvent(
    val username: String,
    val counterType: FollowCounterType,
    val action: CounterAction
)

enum class FollowCounterType {
    FOLLOWERS,
    FOLLOWING
}

enum class CounterAction {
    INCREMENT,
    DECREMENT
}