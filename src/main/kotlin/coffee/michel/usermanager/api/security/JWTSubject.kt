package coffee.michel.usermanager.api.security

import coffee.michel.usermanager.api.UserGroupReadDto

internal data class JWTSubject(
    val id: Int,
    val username: String,
    val groups: Set<UserGroupReadDto>
)
