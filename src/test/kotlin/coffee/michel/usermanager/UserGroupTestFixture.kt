package coffee.michel.usermanager

import coffee.michel.usermanager.api.UserGroupReadDto
import coffee.michel.usermanager.api.UserGroupWriteDto
import coffee.michel.usermanager.domain.UserGroup
import coffee.michel.usermanager.persistence.entity.UserGroupEntity

internal object UserGroupTestFixture {

    fun configureUserGroupModel(block: UserGroupModelBuilder.() -> Unit) =
        UserGroupModelBuilder().apply(block).build()

    data class UserGroupModelCollection(
        val apiReadDto: UserGroupReadDto,
        val apiWriteDto: UserGroupWriteDto,
        val domain: UserGroup,
        val entity: UserGroupEntity,
        val name: String
    )

    class UserGroupModelBuilder {
        var id: Int = 59667328
        var name: String = "TestUserGroup123"

        fun build() = UserGroupModelCollection(
            apiReadDto = UserGroupReadDto(
                id = id,
                name = name
            ),
            apiWriteDto = UserGroupWriteDto(
                id = id,
                name = name
            ),
            domain = UserGroup(
                id = id,
                name = name
            ),
            entity = UserGroupEntity(
                id = id,
                name = name
            ),
            name = name
        )
    }
}
