package coffee.michel.usermanager.api

import kotlinx.serialization.Serializable

@Serializable
internal data class UserGroupReadDto(
    val id: Int,
    val name: String
)
