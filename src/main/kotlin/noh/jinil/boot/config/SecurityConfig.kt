package noh.jinil.boot.config

import noh.jinil.boot.auth.firebase.FirebaseAuthProvider
import noh.jinil.boot.auth.firebase.FirebaseRequestFilter
import noh.jinil.boot.service.FirebaseService
import noh.jinil.boot.service.impl.UserServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@EnableGlobalMethodSecurity(securedEnabled = true)
@Suppress("unused")
class SecurityConfig {

    object Roles {
        private const val ANONYMOUS = "ANONYMOUS"
        private const val USER = "USER"
        const val ADMIN = "ADMIN"

        private const val ROLE_ = "ROLE_"
        const val ROLE_ANONYMOUS = ROLE_ + ANONYMOUS
        const val ROLE_USER = ROLE_ + USER
        const val ROLE_ADMIN = ROLE_ + ADMIN
    }

    /**
     * 스프링은 크게 2가지인터페이스를 구현하여 인증/인가 처리를 도와준다.
     * (AuthenticationProvider,UserDetailsService)
     * UserDetailsService 는 데이터베이스의 유저정보를 불러오는 역활을한다
     * AuthenticationProvider 는 UserDetailsService에서 리턴해준 유저정보와 사용자가 입력한 유저의 비밀번호를 매칭하여 로그인 인증처리를 한다
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Configuration
    class AuthenticationSecurity : GlobalAuthenticationConfigurerAdapter() {

        @Autowired
        @Qualifier(value = UserServiceImpl.NAME)
        private val userService: UserDetailsService? = null

        @Autowired
        private val authProvider: FirebaseAuthProvider? = null

        override fun init(auth: AuthenticationManagerBuilder?) {
            auth?.userDetailsService(userService)
            auth?.authenticationProvider(authProvider)
        }
    }

    @Order(SecurityProperties.DEFAULT_FILTER_ORDER)
    @Configuration
    @EnableWebSecurity
    class ApplicationSecurity : WebSecurityConfigurerAdapter() {

        override fun configure(web: WebSecurity?) {
            web?.ignoring()?.antMatchers("/h2/**", "/css/**", "/script/**", "/image/**", "/scan/**")
            web?.ignoring()?.antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/webjars/**")
        }

        override fun configure(http: HttpSecurity?) {
            http?.addFilterBefore(tokenAuthorizationFilter(), BasicAuthenticationFilter::class.java)?.authorizeRequests()
                    ?.antMatchers("/api/**")?.hasRole(Roles.ADMIN)
                    ?.antMatchers("/**")?.denyAll()
                    ?.and()?.csrf()?.disable()
                    ?.anonymous()?.authorities(Roles.ROLE_ANONYMOUS)
        }

        @Autowired(required = false)
        private val firebaseService: FirebaseService? = null

        private fun tokenAuthorizationFilter() = FirebaseRequestFilter(firebaseService)

//        override fun configure(http: HttpSecurity) {
//            http.authorizeRequests()
//                    .antMatchers("/api/**").permitAll()
//                    .and()
//                    .csrf().ignoringAntMatchers("/api/**")
//        }
    }
}