package coffee.michel.usermanager.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
class SecurityConfiguration() {

    @Configuration
    // FIXME Don't know if i should keep this
    class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {
        override fun configure(web: WebSecurity) {
            web.ignoring().antMatchers(
                "/**"
            )
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    // @Configuration
    class LocalDevelopmentSecurityConfiguration(
        private val authFilter: JwtFilter
    ) : WebSecurityConfigurerAdapter() {
        override fun configure(http: HttpSecurity) {
            http.addFilterAfter(authFilter, AnonymousAuthenticationFilter::class.java)
        }
    }
}
