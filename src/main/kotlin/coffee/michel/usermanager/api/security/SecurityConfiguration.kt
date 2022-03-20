package coffee.michel.usermanager.api.security

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
internal class SecurityConfiguration(
    private val jwtService: JWTService,
    private val passwordEncoder: PasswordEncoder,
    private val userDetailsService: AuthenticationUserDetailsService
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .antMatchers("/subject/login", "/docs/**").permitAll()
            .anyRequest().authenticated()
            .and().addFilter(JwtAuthorizationFilter(jwtService, authenticationManager()))
            .sessionManagement().sessionCreationPolicy(STATELESS)
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService<UserDetailsService>(userDetailsService)
            .passwordEncoder(passwordEncoder)
    }
}
