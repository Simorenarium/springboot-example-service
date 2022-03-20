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

    fun listAllSubjects(): List<Subject> = subjectStorage.list()

    fun getSubject(id: Int): Subject = subjectStorage.get(id)

    fun getSubjectByName(username: String): Subject = subjectStorage.getByName(username)

    fun assignGroupToSubject(id: Int, userGroup: UserGroup): Subject {
        val subject = subjectStorage.get(id)

        return subjectStorage.persist(subject.copy(groups = subject.groups + userGroup))
    }

    fun delete(id: Int) = subjectStorage.delete(id)
}
