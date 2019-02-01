package noh.jinil.boot.service.impl

import com.google.firebase.auth.FirebaseToken
import noh.jinil.boot.config.SecurityConfig
import noh.jinil.boot.domain.entity.RoleEntity
import noh.jinil.boot.domain.entity.UserEntity
import noh.jinil.boot.domain.repository.RoleRepository
import noh.jinil.boot.domain.repository.UserRepository
import noh.jinil.boot.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
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

    //@Transactional
    //@Secured(value = [SecurityConfig.Roles.ROLE_ANONYMOUS])
    override fun registerUser(holder: FirebaseToken): UserEntity? {
        val userEntity = userRepository?.findByUsername(holder.uid)

        return if (userEntity == null) {
            logger.info("registerUser() email: ${holder.email}")
            val data = UserEntity().apply {
                username = holder.uid
                password = "password"
                email = holder.email
                level = 0
                name = holder.name
                type = "firebase"
                authorities = getUserRoles()
            }
            userRepository?.save(data)
        } else {
            null
        }
    }

    override fun verifyUser(holder: FirebaseToken): Boolean {
        logger.info("verifyUser() email: ${holder.email}")
        logger.debug("result: ${loadUserByUsername(holder.uid) != null}")
        return (loadUserByUsername(holder.uid) != null)
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails? {
        logger.info("loadUserByUsername() id: $username")
        if (username == null) {
            return null
        }

        try {
            val userEntity = userRepository?.findByUsername(username) ?: return null
            logger.debug("->userEntity: $userEntity")
            return User(userEntity.username, userEntity.password, userEntity.authorities)
        } catch (e: EmptyResultDataAccessException) {
            e.printStackTrace()
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

private val logger = LoggerFactory.getLogger(UserServiceImpl::class.java)