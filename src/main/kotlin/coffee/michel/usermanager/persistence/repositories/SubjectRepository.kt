package coffee.michel.usermanager.persistence.repositories

import coffee.michel.usermanager.persistence.entity.SubjectEntity
import org.springframework.data.jpa.repository.JpaRepository

internal interface SubjectRepository : JpaRepository<SubjectEntity, Int>
