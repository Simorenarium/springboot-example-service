package coffee.michel.usermanager.persistence.storage

import coffee.michel.usermanager.domain.UserGroup
import coffee.michel.usermanager.exception.UserGroupNotFoundException
import coffee.michel.usermanager.persistence.UserGroupStorage
import coffee.michel.usermanager.persistence.repositories.UserGroupRepository
import coffee.michel.usermanager.persistence.utility.mapToDomain
import coffee.michel.usermanager.persistence.utility.mapToEntity
import org.springframework.stereotype.Service

@Service
internal class UserGroupJpaRepositoryStorage(
    private val userGroupRepository: UserGroupRepository
) : UserGroupStorage {

    override fun list(): List<UserGroup> =
        userGroupRepository.findAll().map { mapToDomain(it) }

    override fun get(id: Int): UserGroup =
        mapToDomain(findEntity(id))

    override fun persist(userGroup: UserGroup): UserGroup =
        mapToDomain(userGroupRepository.save(mapToEntity(userGroup)))

    override fun delete(id: Int) =
        userGroupRepository.delete(findEntity(id))

    private fun findEntity(id: Int) =
        userGroupRepository.findById(id)
            .orElseThrow { UserGroupNotFoundException("UserGroup with id '$id' not found.") }
}
