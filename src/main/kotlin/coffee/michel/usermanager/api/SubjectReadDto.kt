package coffee.michel.usermanager.api

import kotlinx.serialization.Serializable

/**
 * This data class serves as a serializable projection
 * of the actual Domain-Model according to the clients requirements.
 *
 * As it will be received by clients it does not contain information that needs to be secure.
 */
@Serializable
internal data class SubjectReadDto(
    val sub: Int,
    val username: String,
    val groups: Set<UserGroupReadDto>
)
