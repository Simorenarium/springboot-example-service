package coffee.michel.usermanager.security

import coffee.michel.usermanager.domain.Subject
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * This Component supplies the HTTP Server with an Authentication-Filter which
 * TODO check the purpose once the JWT topic clears up
 * [enables JWT Authentication[ and supplies the Context with the logged-in User]]
 *
 * @see <a href="https://javatodev.com/spring-boot-jwt-authentication/">Springboot JWT Example</a>
 */
// @Component
@ExperimentalSerializationApi
class JwtFilter(
    val tokenFactory: TokenFactory
) : UsernamePasswordAuthenticationFilter() {

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication? {
        val creds: Subject = Json.decodeFromStream(request.inputStream)
        // TODO check if user exists

        return authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                creds.username,
                creds.password,
                ArrayList()
            )
        )
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        chain: FilterChain?,
        auth: Authentication
    ) {
        // TODO consider present auth
        // TODO set key and prefix
        response.addHeader(
            "jwt",
            tokenFactory.generate()
        )
    }

    // TODO mabe
}
