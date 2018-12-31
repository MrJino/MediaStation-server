package noh.jinil.boot.exception

import org.springframework.security.authentication.BadCredentialsException

class FirebaseTokenInvalidException(message: String?) : BadCredentialsException(message) {
    private val serialVersionUID = 789949671713648425L
}