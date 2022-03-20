package coffee.michel.usermanager.persistence.storage

import coffee.michel.usermanager.SubjectTestFixture.configureSubjectModel
import coffee.michel.usermanager.exception.SubjectNotFoundException
import coffee.michel.usermanager.persistence.repositories.SubjectRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.Optional

class SubjectJpaRepositoryStorageTest {

    private val subjectRepository = mockk<SubjectRepository>()

    private val sut = SubjectJpaRepositoryStorage(subjectRepository)

    @Test
    fun `list - when the repository returns a list of Subjects, they must be returned`() {
        val (_, _, domainSubject1, entitySubject1) = configureSubjectModel { id = 1 }
        val (_, _, domainSubject2, entitySubject2) = configureSubjectModel { id = 2 }
        val storedSubjects = listOf(entitySubject1, entitySubject2)
        val expectedSubjects = listOf(domainSubject1, domainSubject2)

        every { subjectRepository.findAll() } returns storedSubjects

        val result = sut.list()

        assertThat(result).containsAll(expectedSubjects)
    }

    @Test
    fun `list - when the repository returns an empty list of Subjects, an empty list must be returned`() {
        every { subjectRepository.findAll() } returns emptyList()

        val result = sut.list()

        assertThat(result).isEmpty()
    }

    @Test
    fun `get - when a Subject is requested, it must be returned`() {
        val testId = 99283
        val (_, _, domainSubject, entitySubject) = configureSubjectModel { id = testId }

        every { subjectRepository.findById(testId) } returns Optional.of(entitySubject)

        val result = sut.get(testId)

        assertThat(result).isEqualTo(domainSubject)
    }

    @Test
    fun `get - when a Subject is requested and not found, an exception must be thrown`() {
        val testId = 99283
        every { subjectRepository.findById(testId) } returns Optional.empty()

        assertThrows<SubjectNotFoundException>("Subject with id '$testId' not found.") { sut.get(testId) }
    }

    @Test
    fun `persist - when a Subject is persisted, it must return the persisted entity`() {
        val (_, _, domainSubject, entitySubject) = configureSubjectModel { }
        every { subjectRepository.save(entitySubject) } returns entitySubject

        val result = sut.persist(domainSubject)

        assertThat(result).isEqualTo(domainSubject)
    }

    @Test
    fun delete() {
        val testId = 99283
        val (_, _, _, entitySubject) = configureSubjectModel { id = testId }

        every { subjectRepository.findById(testId) } returns Optional.of(entitySubject)
        every { subjectRepository.delete(entitySubject) } just Runs

        sut.delete(testId)

        verify { subjectRepository.delete(entitySubject) }
    }

    @Test
    fun `delete - when a Subject should be deleted but was not found, an exception must be thrown`() {
        val testId = 99283

        every { subjectRepository.findById(testId) } returns Optional.empty()

        assertThrows<SubjectNotFoundException>("Subject with id '$testId' not found.") { sut.delete(testId) }
    }
}
