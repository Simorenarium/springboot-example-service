package coffee.michel.usermanager.persistence.repositories

import coffee.michel.usermanager.persistence.entity.SubjectEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

internal interface SubjectRepository : JpaRepository<SubjectEntity, Int> {

    /**
     * Searches for a Subject by its username.
     *
     * @return An Optional containing the sough Subject or an empty Optional when it couldn't be found.
     */
    fun findByUsername(username: String): Optional<SubjectEntity>
}
