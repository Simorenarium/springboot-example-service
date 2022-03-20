package coffee.michel.usermanager.api.security

import coffee.michel.usermanager.domain.service.SubjectService
import coffee.michel.usermanager.exception.SubjectNotFoundException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
internal class AuthenticationUserDetailsService(
    private val subjectService: SubjectService
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails =
        try {
            subjectService.getSubjectByName(username)
        } catch (e: SubjectNotFoundException) {
            throw UsernameNotFoundException(e.message, e)
        }
}
