package coffee.michel.usermanager.persistence.utility

import coffee.michel.usermanager.domain.Subject
import coffee.michel.usermanager.domain.UserGroup
import coffee.michel.usermanager.persistence.entity.SubjectEntity
import coffee.michel.usermanager.persistence.entity.UserGroupEntity

internal fun mapToEntity(subject: Subject): SubjectEntity =
    SubjectEntity(
        id = subject.id,
        username = subject.username,
        password = subject.password,
        groups = subject.groups.map { mapToEntity(it) }.toSet()
    )

internal fun mapToEntity(userGroup: UserGroup): UserGroupEntity =
    UserGroupEntity(
        id = userGroup.id,
        name = userGroup.name
    )

internal fun mapToDomain(subject: SubjectEntity): Subject =
    Subject(
        id = subject.id,
        username = subject.username,
        password = subject.password,
        groups = subject.groups.map { mapToDomain(it) }.toSet()
    )

internal fun mapToDomain(userGroup: UserGroupEntity): UserGroup =
    UserGroup(
        id = userGroup.id,
        name = userGroup.name
    )
