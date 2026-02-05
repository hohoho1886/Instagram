package org.ninh.instaclone.service


import jakarta.transaction.Transactional
import org.mindrot.jbcrypt.BCrypt
import org.ninh.instaclone.dto.RegisterUserRequest
import org.ninh.instaclone.dto.UserResponse
import org.ninh.instaclone.model.User
import org.ninh.instaclone.repository.UserDetailsRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service

const val LOGS_ROUND = 12
@Service
class UserService(
    private val userDetailsRepository: UserDetailsRepository
) {
    @Transactional
    fun registerUser(request: RegisterUserRequest): UserResponse {
        val hashedPassword = BCrypt.hashpw(request.password, BCrypt.gensalt(LOGS_ROUND))


        if (hashedPassword.isNullOrEmpty()) {
            throw IllegalStateException("Password encoding returned blank")
        }

        val user = User(
            username = request.username,
            passwordHash = hashedPassword,
            email = request.email
        )

        try {
            userDetailsRepository.save(user)
        } catch (_: DataIntegrityViolationException) {
            throw RuntimeException("Username already exists")
        }

        return UserResponse(
            userId = user.userId,
            username = user.username
        )
    }

    // TODO: update record
    /*
    @Transactional
    fun updateProfilePicture(username: String, profilePictureUrl: String) {
        // JPA Style: Fetch, modify, and let @Transactional handle the save (Dirty Checking)
        val user = userDetailsRepository.findByUsername(username).get()
            ?: throw RuntimeException("User not found")

        user.profilePictureUrl = profilePictureUrl
        // You don't actually need to call .save(user) here because
        // @Transactional will auto-commit the changes!
    }

     */
}



