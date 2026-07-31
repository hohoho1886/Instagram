package org.ninh.insta_backend.account.repository

import org.ninh.insta_backend.account.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface UserDetailsRepository: JpaRepository<User, UUID>{
    fun findByUsername(username: String): Optional<User>
    fun existsByUsername(username: String): Boolean
}
