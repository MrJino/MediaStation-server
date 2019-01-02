package noh.jinil.boot.service.impl

import noh.jinil.boot.config.SecurityConfig
import noh.jinil.boot.domain.entity.RoleEntity
import noh.jinil.boot.domain.entity.UserEntity
import noh.jinil.boot.domain.repository.RoleRepository
import noh.jinil.boot.domain.repository.UserRepository
import noh.jinil.boot.service.UserService
import noh.jinil.boot.service.shared.RegisterUserInit
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.annotation.PostConstruct

@Service(value = UserServiceImpl.NAME)
class UserServiceImpl : UserService {

    companion object {
        const val NAME = "UserService"
    }

    @Autowired
    private val userRepository: UserRepository? = null

    @Autowired
    private val roleRepository: RoleRepository? = null

    @PostConstruct
    fun init() {
        userRepository?.deleteAll()
        roleRepository?.deleteAll()

        if (userRepository?.count() == 0L) {
            val adminEntity = UserEntity().apply {
                username = "1iTC5nOXFmeakKyM1NQ7EJr0TmB3"
                password = "admin"
                email = "for1self@gmail.com"
                authorities = getAdminRoles()
            }
            userRepository.save(adminEntity)

            val userEntity = UserEntity().apply {
                username = "aaa"
                password = "user"
                email = "gabsikee@gmail.com"
                authorities = getUserRoles()
            }
            userRepository.save(userEntity)
        }
    }

    private fun getAdminRoles(): List<RoleEntity> {
        return Collections.singletonList(getRole(SecurityConfig.Roles.ROLE_ADMIN))
    }

    private fun getUserRoles(): List<RoleEntity> {
        return Collections.singletonList(getRole(SecurityConfig.Roles.ROLE_USER))
    }

    private fun getRole(authority: String): RoleEntity {
        return roleRepository?.findByAuthority(authority) ?: RoleEntity(authority)
    }

    @Transactional
    @Secured(value = [SecurityConfig.Roles.ROLE_ANONYMOUS])
    override fun registerUser(init: RegisterUserInit): UserEntity {
        val userLoaded = userRepository?.findByUsername(init.username)

        return if (userLoaded == null) {
            val userEntity = UserEntity().apply {
                username = init.username
                email = init.email
                password = UUID.randomUUID().toString()
                authorities = getUserRoles()
            }
            userRepository?.save(userEntity)
            userEntity
        } else {
            userLoaded
        }
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails? {
        if (username == null) {
            return null
        }

        try {
            val userDetails = userRepository?.findByUsername(username) ?: return null
            return User(userDetails.username, userDetails.password, userDetails.authorities)
        } catch (e: EmptyResultDataAccessException) {
            return null
        }

        /*
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		for (GrantedAuthority role : userDetails.getAuthorities()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}
		*/
    }
}