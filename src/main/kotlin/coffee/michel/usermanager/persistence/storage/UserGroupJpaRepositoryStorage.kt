package coffee.michel.usermanager.persistence.storage

import coffee.michel.usermanager.domain.UserGroup
import coffee.michel.usermanager.persistence.UserGroupStorage
import org.springframework.stereotype.Service

@Service
internal class UserGroupJpaRepositoryStorage : UserGroupStorage {
    override fun list(): List<UserGroup> {
        TODO("Not yet implemented")
    }

    override fun get(id: Int): UserGroup {
        TODO("Not yet implemented")
    }

    override fun persist(subject: UserGroup): UserGroup {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int): UserGroup {
        TODO("Not yet implemented")
    }
}
