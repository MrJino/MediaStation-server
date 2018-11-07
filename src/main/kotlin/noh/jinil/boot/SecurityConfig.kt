package noh.jinil.boot

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(web: WebSecurity?) {
        web?.ignoring()?.antMatchers("/h2/**", "/css/**", "/script/**", "/image/**")
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers("/api/**").permitAll()
                .and()
                .csrf().ignoringAntMatchers("/api/**")
    }
}