package coffee.michel.usermanager.persistence

import coffee.michel.usermanager.domain.Subject

internal interface SubjectStorage {

    fun list(): List<Subject>
    fun get(id: Int): Subject
    fun persist(subject: Subject): Subject
    fun delete(id: Int)
}
