package org.ninh.instaclone.function

import org.ninh.instaclone.dto.UploadRequestFromUser
import org.ninh.instaclone.service.UploadPublisher
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import java.util.function.Function

@Component
@Validated
class UploadFunction(
    private val uploadPublisher: UploadPublisher
) {
    @Bean
    fun upload(): Function<UploadRequestFromUser, String> = Function{ request ->
        if (!isValidUuid(request.postId)) {
            throw IllegalArgumentException("Invalid postId UUID")
        }
        if (request.authorId.isBlank()) {
            throw IllegalArgumentException("Invalid author username")
        }
        uploadPublisher.publish(request)
        "Message published successfully"

    }
    fun isValidUuid(value: String): Boolean =
        try {
            java.util.UUID.fromString(value)
            true
        } catch (_: IllegalArgumentException) {
            false
        }

}