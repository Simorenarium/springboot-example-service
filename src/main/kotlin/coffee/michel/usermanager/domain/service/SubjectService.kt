package coffee.michel.usermanager.domain.service

import coffee.michel.usermanager.domain.Subject
import coffee.michel.usermanager.domain.UserGroup
import coffee.michel.usermanager.persistence.SubjectStorage
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * Instances of this class serve all functionality regarding the Subject.
 *
 * Its purpose is to encapsulate the logic behind specific features in function
 * which have the name of the implemented feature.
 * This should make it recognizable where to look first.
 */
@Service
@Transactional
internal class SubjectService(
    private val subjectStorage: SubjectStorage,
    private val passwordEncoder: PasswordEncoder
) {

    /**
     * Performs the registriation of a user.
     * The given subject will be written to the storage with an encoded version of the password.
     *
     * @param The Subject representing the user that is to be registered. Its id will be overwritten.
     * @return The Subject as it was written to the storage, with a new id.
     */
    fun register(subject: Subject): Subject =
        subjectStorage.persist(subject.copy(password = passwordEncoder.encode(subject.password)))

    fun login(username: String, password: String): Boolean {
        val persistedSubject = subjectStorage.getByName(username)

        return passwordEncoder.matches(password, persistedSubject.password)
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
