package noh.jinil.boot.exception

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException

class FirebaseUserNotExistsException : AuthenticationCredentialsNotFoundException("User Not Found") {
    private val serialVersionUID = 789949671713648425L
}