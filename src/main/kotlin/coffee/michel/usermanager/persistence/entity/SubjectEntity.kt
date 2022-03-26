package coffee.michel.usermanager.persistence.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "Subject")
internal data class SubjectEntity(
    @Id
    @GeneratedValue
    val id: Int,
    @Column(unique = true)
    val username: String,
    val password: String,
    @OneToMany
    val groups: Set<UserGroupEntity>
) {

    override fun toString(): String {
        return "SubjectEntity(id=$id, username='$username', password='<hidden>', groups=$groups)"
    }
}
