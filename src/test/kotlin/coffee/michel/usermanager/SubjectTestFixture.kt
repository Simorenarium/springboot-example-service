package coffee.michel.usermanager

import coffee.michel.usermanager.api.SubjectReadDto
import coffee.michel.usermanager.api.SubjectWriteDto
import coffee.michel.usermanager.api.utility.mapToReadDto
import coffee.michel.usermanager.domain.Subject
import coffee.michel.usermanager.domain.UserGroup
import coffee.michel.usermanager.persistence.entity.SubjectEntity
import coffee.michel.usermanager.persistence.utility.mapToEntity

internal object SubjectTestFixture {

    fun configureSubjectModel(block: SubjectModelBuilder.() -> Unit) =
        SubjectModelBuilder().apply(block).build()

    data class SubjectModelCollection(
        val apiReadDto: SubjectReadDto,
        val apiWriteDto: SubjectWriteDto,
        val domain: Subject,
        val entity: SubjectEntity
    )

    class SubjectModelBuilder {
        var id: Int = 77268953
        var username: String = "TestUser123"
        var password: String = "PasswordCheckCheck123"
        var userGroups: Set<UserGroup> = emptySet()

        fun build() = SubjectModelCollection(
            apiReadDto = SubjectReadDto(
                sub = id,
                username = username,
                groups = userGroups.map { mapToReadDto(it) }.toSet()
            ),
            apiWriteDto = SubjectWriteDto(
                username = username,
                password = password
            ),
            domain = Subject(
                id = id,
                username = username,
                password = password,
                groups = userGroups
            ),
            entity = SubjectEntity(
                id = id,
                username = username,
                password = password,
                groups = userGroups.map { mapToEntity(it) }.toSet()
            )
        )
    }
}
