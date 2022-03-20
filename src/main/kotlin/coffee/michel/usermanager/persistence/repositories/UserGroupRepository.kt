package coffee.michel.usermanager.persistence.repositories

import coffee.michel.usermanager.persistence.entity.UserGroupEntity
import org.springframework.data.jpa.repository.JpaRepository

internal interface UserGroupRepository : JpaRepository<UserGroupEntity, Int>
