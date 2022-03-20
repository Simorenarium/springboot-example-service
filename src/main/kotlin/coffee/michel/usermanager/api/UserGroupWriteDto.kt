package coffee.michel.usermanager.api

import kotlinx.serialization.Serializable

@Serializable
internal data class UserGroupWriteDto(
    val id: Int,
    val name: String
)
