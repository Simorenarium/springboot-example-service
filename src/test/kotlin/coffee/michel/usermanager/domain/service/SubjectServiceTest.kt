package coffee.michel.usermanager.domain.service

import coffee.michel.usermanager.SubjectTestFixture.configureSubjectModel
import coffee.michel.usermanager.UserGroupTestFixture.configureUserGroupModel
import coffee.michel.usermanager.persistence.SubjectStorage
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder

internal class SubjectServiceTest {

    private val subjectStorage = mockk<SubjectStorage>()
    private val passwordEncoder = mockk<PasswordEncoder>()

    private val sut = SubjectService(subjectStorage, passwordEncoder)

    @Test
    fun `register - when a subject is registered, their password must be encoded`() {
        val (_, _, domainSubject) = configureSubjectModel {}
        val encodedPassword = "this password is encoded"

        every { passwordEncoder.encode(domainSubject.password) } returns encodedPassword
        every { subjectStorage.persist(any()) } returnsArgument 0

        val result = sut.register(domainSubject)

        assertThat(result).isEqualTo(domainSubject.copy(password = encodedPassword))
    }

    @Test
    fun `login - when a subject is logging in and their password matches, true must be returned`() {
        val (_, _, domainSubject) = configureSubjectModel {}
        val testPassword = "test password 123"

        every { subjectStorage.getByName(domainSubject.username) } returns domainSubject
        every { passwordEncoder.matches(testPassword, domainSubject.password) } returns true

        val result = sut.login(domainSubject.username, testPassword)

        assertThat(result).isTrue
    }

    @Test
    fun `login - when a subject is logging in and their password does not match, false must be returned`() {
        val (_, _, domainSubject) = configureSubjectModel {}
        val testPassword = "test password 123"

        every { subjectStorage.getByName(domainSubject.username) } returns domainSubject
        every { passwordEncoder.matches(testPassword, domainSubject.password) } returns false

        val result = sut.login(domainSubject.username, testPassword)

        assertThat(result).isFalse
    }

    @Test
    fun `listAllSubjects - if a list of all subjects is requested, it must be returned`() {
        val allSubjects = listOf(
            configureSubjectModel { id = 1 }.domain,
            configureSubjectModel { id = 2 }.domain
        )

        every { subjectStorage.list() } returns allSubjects

        val result = sut.listAllSubjects()

        assertThat(result).containsAll(allSubjects)
    }

    @Test
    fun `listAllSubjects - if a list of all subjects is requested but it's empty, the empty result must be returned`() {
        every { subjectStorage.list() } returns emptyList()

        val result = sut.listAllSubjects()

        assertThat(result).isEmpty()
    }

    @Test
    fun `getSubject - if a specific subject is requested by its id, it must be returned`() {
        val testId = 44652
        val (_, _, domainSubject) = configureSubjectModel { id = testId }

        every { subjectStorage.get(testId) } returns domainSubject

        val result = sut.getSubject(testId)

        assertThat(result).isEqualTo(domainSubject)
    }

    @Test
    fun `getSubject - if a specific subject is requested by its username, it must be returned`() {
        val testUsername = "this user only exists for this test"
        val (_, _, domainSubject) = configureSubjectModel { username = testUsername }

        every { subjectStorage.getByName(testUsername) } returns domainSubject

        val result = sut.getSubjectByName(testUsername)

        assertThat(result).isEqualTo(domainSubject)
    }

    @Test
    fun `assignGroupToSubject - if a group is to be assigned to the subject, the subject has to be persisted with the newly assigned group`() {
        val (_, _, domainUserGroup) = configureUserGroupModel { }
        val (_, _, domainSubject) = configureSubjectModel { }

        every { subjectStorage.get(domainSubject.id) } returns domainSubject
        every { subjectStorage.persist(any()) } returnsArgument 0

        val result = sut.assignGroupToSubject(domainSubject.id, domainUserGroup)

        assertThat(result.id).isEqualTo(domainSubject.id)
        assertThat(result.username).isEqualTo(domainSubject.username)
        assertThat(result.password).isEqualTo(domainSubject.password)
        assertThat(result.groups).contains(domainUserGroup)
    }

    @Test
    fun `delete - if the delete of a subject is requested, it must be performed`() {
        val testId = 55698

        every { subjectStorage.delete(testId) } just Runs

        sut.delete(testId)

        verify { subjectStorage.delete(testId) }
    }
}
