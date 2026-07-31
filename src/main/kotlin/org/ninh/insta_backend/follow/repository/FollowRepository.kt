package org.ninh.insta_backend.follow.repository

import org.ninh.insta_backend.follow.model.Follow
import org.ninh.insta_backend.follow.model.FollowId
import org.springframework.data.jpa.repository.JpaRepository

interface FollowRepository: JpaRepository<Follow, FollowId> {}
