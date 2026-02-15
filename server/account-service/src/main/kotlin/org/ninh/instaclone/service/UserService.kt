package org.ninh.instaclone.service

import jakarta.transaction.Transactional
import org.mindrot.jbcrypt.BCrypt
import org.ninh.instaclone.dto.AuthRequest
import org.ninh.instaclone.dto.AuthResponse
import org.ninh.instaclone.dto.RegisterRequest
import org.ninh.instaclone.dto.RegisterResponse
import org.ninh.instaclone.model.User
import org.ninh.instaclone.repository.UserDetailsRepository
import org.springframework.stereotype.Service
import java.util.Optional
import org.ninh.instaclone.message.Messages
import org.ninh.instaclone.utils.JwtUtils
import org.postgresql.util.PSQLException

const val LOGS_ROUND = 12

@Service
class UserService(
    private val userDetailsRepository: UserDetailsRepository,
    private val messages: Messages,
    private val jwtUtils: JwtUtils
) {
    @Transactional
    fun registerUser(request: RegisterRequest): RegisterResponse {
        val hashedPassword = BCrypt.hashpw(request.password, BCrypt.gensalt(LOGS_ROUND))

        if (hashedPassword.isNullOrEmpty()) {
            return RegisterResponse(error = messages.passwordEncodingError())
        }

        val user =
            User(username = request.username, passwordHash = hashedPassword, email = request.email)

        if (userDetailsRepository.existsByUsername(username = user.username)) {
            return RegisterResponse(error = messages.usernameAlreadyExist())
        }

        return try {
            userDetailsRepository.save(user)
            RegisterResponse(userId = user.userId, username = user.username)
        } catch (e: PSQLException) { // TODO: it seems that save does not throw when user register with same username
            RegisterResponse(error = e.message)
        }

    }

    fun findByUsername(username: String): Optional<User> {
        return userDetailsRepository.findByUsername(username)
    }

    fun loginUser(authRequest: AuthRequest): AuthResponse {
        try {
            val userOptional = findByUsername(authRequest.username)
            val user = userOptional.get()
            val passwordMatches = BCrypt.checkpw(authRequest.password, user.passwordHash)
            if (!passwordMatches) {
                return AuthResponse(error = messages.invalidCredentials())
            }
            val token = jwtUtils.generateToken(user.username)
            return AuthResponse(jwtToken = token)
        } catch (_: NoSuchElementException) {
            return AuthResponse(error = messages.invalidCredentials())
        } catch (ex: Exception) {
            ex.printStackTrace()
            return AuthResponse(error = messages.unexpectedErr())
        }
    }
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

