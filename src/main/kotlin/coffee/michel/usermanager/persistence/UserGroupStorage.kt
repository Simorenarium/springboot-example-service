package coffee.michel.usermanager.persistence

import coffee.michel.usermanager.domain.UserGroup

internal interface UserGroupStorage {

    fun list(): List<UserGroup>
    fun get(id: Int): UserGroup
    fun persist(subject: UserGroup): UserGroup
    fun delete(id: Int): UserGroup
}
