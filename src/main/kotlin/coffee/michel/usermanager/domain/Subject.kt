package coffee.michel.usermanager.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

internal data class Subject(
    val id: Int,
    private val username: String,
    private val password: String,
    val groups: Set<UserGroup>
) : UserDetails {

    override fun getPassword() = password

    override fun getUsername() = username

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        groups.map { SimpleGrantedAuthority(it.name) }.toMutableList()

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true

    override fun toString(): String {
        return "Subject(id=$id, username='$username', password='<hidden>', groups=$groups)"
    }
}
