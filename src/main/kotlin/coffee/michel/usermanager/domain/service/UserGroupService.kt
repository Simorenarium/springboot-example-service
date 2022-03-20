package coffee.michel.usermanager.domain.service

import coffee.michel.usermanager.domain.UserGroup
import coffee.michel.usermanager.persistence.UserGroupStorage
import org.springframework.stereotype.Service

/**
 * Instances of this class serve all functionality regarding the UserGroup.
 */
@Service
internal class UserGroupService(
    private val userGroupStorage: UserGroupStorage
) {

    fun listAllUserGroups(): List<UserGroup> = userGroupStorage.list()

    fun getUserGroup(id: Int): UserGroup = userGroupStorage.get(id)

    fun createUserGroup(group: UserGroup): UserGroup = userGroupStorage.persist(group)

    fun deleteUserGroup(id: Int) = userGroupStorage.delete(id)
}
