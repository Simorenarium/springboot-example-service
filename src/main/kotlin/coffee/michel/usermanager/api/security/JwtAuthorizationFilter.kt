package coffee.michel.usermanager.api.security

import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * This Component supplies the HTTP Server with an Authentication-Filter which
 * enables JWT Authentication via a "Bearer" Token.
 *
 * @see <a href="https://javatodev.com/spring-boot-jwt-authentication/">Springboot JWT Example</a>
 */
internal class JwtAuthorizationFilter(
    private val jwtService: JWTService,
    authenticationManager: AuthenticationManager
) : BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val rawJWT = getRawJWT(request)
        if (rawJWT != null)
            handleJWT(rawJWT)
        else
            handleAnonymousUser()

        filterChain.doFilter(request, response)
    }

    private fun handleJWT(rawJWT: String) {
        val jwt = jwtService.parseJWT(rawJWT)

        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            jwt.username, null, jwt.groups.map { SimpleGrantedAuthority(it.name) }
        )
    }

    private fun handleAnonymousUser() {
        // TODO If the principal is an object, consider using the subject
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
            "anonymous", null, emptyList()
        )
    }

    private fun getRawJWT(request: HttpServletRequest) =
        // TODO check for "Bearer" keyword and use that as split point
        request.getHeaders(AUTHORIZATION).toList()
            .find { it.startsWith("Bearer") }
            ?.substring(6)
    // TODO base64 decode?
}
