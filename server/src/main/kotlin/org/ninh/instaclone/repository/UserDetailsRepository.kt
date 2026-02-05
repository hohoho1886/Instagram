package org.ninh.instaclone.repository

import org.ninh.instaclone.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserDetailsRepository : JpaRepository<User, String> {
    fun findByUsername(username: String): Optional<User>
}