package noh.jinil.boot.service

import noh.jinil.boot.domain.model.UserEntity
import noh.jinil.boot.service.shared.RegisterUserInit
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService : UserDetailsService {
    fun registerUser(init: RegisterUserInit): UserEntity
}