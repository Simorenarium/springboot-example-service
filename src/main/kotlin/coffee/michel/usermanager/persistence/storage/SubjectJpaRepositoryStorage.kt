package coffee.michel.usermanager.persistence.storage

import coffee.michel.usermanager.domain.Subject
import coffee.michel.usermanager.exception.SubjectAlreadyExistsException
import coffee.michel.usermanager.exception.SubjectNotFoundException
import coffee.michel.usermanager.persistence.SubjectStorage
import coffee.michel.usermanager.persistence.entity.SubjectEntity
import coffee.michel.usermanager.persistence.repositories.SubjectRepository
import coffee.michel.usermanager.persistence.utility.mapToDomain
import coffee.michel.usermanager.persistence.utility.mapToEntity
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service

@Service
internal class SubjectJpaRepositoryStorage(
    private val subjectRepository: SubjectRepository
) : SubjectStorage {
    override fun list(): List<Subject> =
        subjectRepository.findAll().map { mapToDomain(it) }

    override fun get(id: Int): Subject =
        mapToDomain(findEntity(id))

    override fun getByName(username: String): Subject =
        subjectRepository.findByUsername(username)
            .map { mapToDomain(it) }
            .orElseThrow { SubjectNotFoundException("Subject with name '$username' not found.") }

    override fun persist(subject: Subject): Subject = try {
        mapToDomain(subjectRepository.save(mapToEntity(subject)))
    } catch (e: DataIntegrityViolationException) {
        when (e.mostSpecificCause) {
            is ConstraintViolationException -> throw SubjectAlreadyExistsException("Subject with username '${subject.username}' already exists.")
            else -> throw e
        }
    }

    override fun delete(id: Int) =
        subjectRepository.delete(findEntity(id))

    private fun findEntity(id: Int): SubjectEntity =
        subjectRepository.findById(id)
            .orElseThrow { SubjectNotFoundException("Subject with id '$id' not found.") }
}
