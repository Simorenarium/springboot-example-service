package coffee.michel.usermanager.persistence.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "UserGroup")
internal data class UserGroupEntity(
    @Id
    @GeneratedValue
    val id: Int,
    val name: String
)
