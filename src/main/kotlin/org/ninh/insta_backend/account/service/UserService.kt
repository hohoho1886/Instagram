package org.ninh.insta_backend.account.service

import jakarta.transaction.Transactional
import org.mindrot.jbcrypt.BCrypt
import org.ninh.insta_backend.account.dto.AuthRequest
import org.ninh.insta_backend.account.dto.AuthResponse
import org.ninh.insta_backend.account.dto.RegisterRequest
import org.ninh.insta_backend.account.dto.RegisterResponse
import org.ninh.insta_backend.account.model.User
import org.ninh.insta_backend.account.repository.UserDetailsRepository
import org.ninh.insta_backend.account.utils.JwtUtils
import org.springframework.stereotype.Service
import org.postgresql.util.PSQLException

const val LOGS_ROUND = 12

@Service
class UserService(
    private val userDetailsRepository: UserDetailsRepository,
    private val jwtUtils: JwtUtils
) {
    @Transactional
    open fun registerUser(request: RegisterRequest): RegisterResponse {
        val hashedPassword = BCrypt.hashpw(request.password, BCrypt.gensalt(LOGS_ROUND))

        if (hashedPassword.isNullOrEmpty()) {
            return RegisterResponse(error = "Password encoding returned blank")
        }

        val user =
            User(username = request.username, passwordHash = hashedPassword)

        if (userDetailsRepository.existsByUsername(username = user.username)) {
            return RegisterResponse(error = "Username already exists")
        }

        return try {
            userDetailsRepository.save(user)
            RegisterResponse(userId = user.userId, username = user.username)
        } catch (e: PSQLException) { // TODO: it seems that save does not throw when user register with same username
            RegisterResponse(error = e.message)
        }

    }

    fun loginUser(authRequest: AuthRequest): AuthResponse {
        try {
            val userOptional = userDetailsRepository.findByUsername(authRequest.username)
            val user = userOptional.get()
            val passwordMatches = BCrypt.checkpw(authRequest.password, user.passwordHash)
            if (!passwordMatches) {
                return AuthResponse(error = "Invalid username or password")
            }
            val token = jwtUtils.generateToken(user.username)
            return AuthResponse(jwtToken = token)
        } catch (_: NoSuchElementException) {
            return AuthResponse(error = "Invalid username or password")
        } catch (ex: Exception) {
            ex.printStackTrace()
            return AuthResponse(error = "An unexpected error occurred during login")
        }
    }
}
