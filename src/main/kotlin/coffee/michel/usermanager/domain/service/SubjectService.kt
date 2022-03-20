package coffee.michel.usermanager.domain.service

import coffee.michel.usermanager.domain.Subject
import coffee.michel.usermanager.domain.UserGroup
import coffee.michel.usermanager.persistence.SubjectStorage
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

/**
 * Instances of this class serve all functionality regarding the Subject.
 */
@Service
internal class SubjectService(
    private val subjectStorage: SubjectStorage,
    private val passwordEncoder: PasswordEncoder
) {

    fun register(subject: Subject): Subject =
        subjectStorage.persist(subject.copy(password = passwordEncoder.encode(subject.password)))

    fun login(subject: Subject): Boolean {
        val persistedSubject = subjectStorage.get(subject.id)

        return passwordEncoder.matches(subject.password, persistedSubject.password)
    }

    fun listAllSubjects(): List<Subject> {
        TODO("implement")
    }

    fun getSubject(id: Int): Subject {
        TODO("implement")
    }

    fun assignGroupToSubject(id: Int, userGroup: UserGroup): Subject {
        TODO("implement")
    }

    fun delete(id: Int) {
        TODO("implement")
    }
}
