package coffee.michel.usermanager.api.security

import coffee.michel.usermanager.api.UserGroupReadDto
import kotlinx.serialization.Serializable

@Serializable
internal data class JWTSubject(
    val sub: Int,
    val username: String,
    val groups: Set<UserGroupReadDto>
)
