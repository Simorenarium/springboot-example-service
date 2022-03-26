package coffee.michel.usermanager.api.utility

import coffee.michel.usermanager.api.SubjectReadDto
import coffee.michel.usermanager.api.SubjectWriteDto
import coffee.michel.usermanager.api.UserGroupReadDto
import coffee.michel.usermanager.api.UserGroupWriteDto
import coffee.michel.usermanager.domain.Subject
import coffee.michel.usermanager.domain.UserGroup

internal fun mapToReadDto(subject: Subject): SubjectReadDto =
    SubjectReadDto(
        sub = subject.id,
        username = subject.username,
        groups = subject.groups.map { mapToReadDto(it) }.toSet()
    )

internal fun mapToReadDto(userGroup: UserGroup): UserGroupReadDto =
    UserGroupReadDto(
        id = userGroup.id,
        name = userGroup.name
    )

internal fun mapToDomain(subject: SubjectWriteDto) =
    Subject(
        id = -1,
        username = subject.username,
        password = subject.password,
        groups = emptySet()
    )

internal fun mapToDomain(userGroup: UserGroupWriteDto) =
    UserGroup(
        id = userGroup.id,
        name = userGroup.name
    )

internal fun mapToDomain(userGroup: String) =
    UserGroup(
        id = -1,
        name = userGroup
    )
