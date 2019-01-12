package noh.jinil.boot.auth.firebase

import noh.jinil.boot.exception.FirebaseUserNotExistsException
import noh.jinil.boot.service.impl.UserServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class FirebaseAuthProvider : AuthenticationProvider {

    @Autowired
    @Qualifier(value = UserServiceImpl.NAME)
    private val userService: UserDetailsService? = null

    override fun supports(authentication: Class<*>): Boolean {
        return FirebaseAuthToken::class.java.isAssignableFrom(authentication)
    }

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication? {
        if (!supports(authentication::class.java)) {
            return null
        }

        var authenticationToken = authentication as FirebaseAuthToken
        val details = userService?.loadUserByUsername(authenticationToken.name)
                ?: throw FirebaseUserNotExistsException()

        logger.debug("authenticate() authorities: ${details.authorities}")
        authenticationToken = FirebaseAuthToken(details, authentication.credentials, details.authorities)

        return authenticationToken
    }
}

private val logger = LoggerFactory.getLogger(FirebaseAuthProvider::class.java)