package noh.jinil.boot.service

import com.google.firebase.auth.FirebaseToken
import noh.jinil.boot.domain.entity.UserEntity
import noh.jinil.boot.service.shared.RegisterUserInit
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService : UserDetailsService {
    fun registerUser(holder: FirebaseToken): UserEntity?
    fun verifyUser(holder: FirebaseToken): Boolean
}