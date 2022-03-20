package coffee.michel.usermanager.persistence.storage

import coffee.michel.usermanager.domain.Subject
import coffee.michel.usermanager.persistence.SubjectStorage
import org.springframework.stereotype.Service

@Service
internal class SubjectJpaRepositoryStorage : SubjectStorage {
    override fun list(): List<Subject> {
        TODO("Not yet implemented")
    }

    override fun get(id: Int): Subject {
        TODO("Not yet implemented")
    }

    override fun persist(subject: Subject): Subject {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }
}
