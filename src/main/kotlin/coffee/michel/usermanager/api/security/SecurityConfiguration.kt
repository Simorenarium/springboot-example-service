package coffee.michel.usermanager.api.security

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import coffee.michel.usermanager.api.controller.SubjectController.Companion.LOGIN_SUBPATH as SUBJECT_LOGIN
import coffee.michel.usermanager.api.controller.SubjectController.Companion.PATH as SUBJECT_PATH

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
internal class SecurityConfiguration(
    private val jwtService: JWTService,
    private val passwordEncoder: PasswordEncoder,
    private val userDetailsService: AuthenticationUserDetailsService
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers(SUBJECT_PATH, "$SUBJECT_PATH/$SUBJECT_LOGIN").permitAll()
            .antMatchers("/docs/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilter(JwtAuthorizationFilter(jwtService, authenticationManager()))
            .sessionManagement().sessionCreationPolicy(STATELESS)
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService<UserDetailsService>(userDetailsService)
            .passwordEncoder(passwordEncoder)
    }
}
