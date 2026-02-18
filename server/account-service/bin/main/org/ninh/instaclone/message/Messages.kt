package org.ninh.instaclone.message

import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import java.util.Locale

@Component
class Messages(
    private val messageSource: MessageSource,
){
    fun invalidCredentials(): String =
        messageSource.getMessage("auth.cre.invalid", null, Locale.ENGLISH)

    fun unexpectedErr(): String =
        messageSource.getMessage("auth.error.unexpected", null, Locale.ENGLISH)

    fun passwordEncodingError(): String =
        messageSource.getMessage("register.password.encoding", null, Locale.ENGLISH)

    fun usernameAlreadyExist(): String =
        messageSource.getMessage("register.username.exist", null, Locale.ENGLISH)

}
