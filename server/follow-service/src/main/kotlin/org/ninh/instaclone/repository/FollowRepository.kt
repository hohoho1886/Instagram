package org.ninh.instaclone.repository

import org.ninh.instaclone.model.Follow
import org.ninh.instaclone.model.FollowId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FollowRepository: JpaRepository<Follow, FollowId>