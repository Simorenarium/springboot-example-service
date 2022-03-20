package coffee.michel.usermanager.api.controller

import coffee.michel.usermanager.SubjectTestFixture.configureSubjectModel
import coffee.michel.usermanager.UserGroupTestFixture.configureUserGroupModel
import coffee.michel.usermanager.domain.service.SubjectService
import coffee.michel.usermanager.exception.SubjectNotFoundException
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CREATED
import org.springframework.security.crypto.password.PasswordEncoder

class SubjectControllerTest {

    private val subjectService = mockk<SubjectService>()
    private val passwordEncoder = mockk<PasswordEncoder>()

    private val sut = SubjectController(subjectService)

    @Test
    fun `allUsers - when a list of users is requested, they must be returned`() {
        val (readDto1, _, domainSubject1) = configureSubjectModel { id = 1 }
        val (readDto2, _, domainSubject2) = configureSubjectModel { id = 2 }
        val storedSubjects = listOf(domainSubject1, domainSubject2)
        val expectedSubjects = listOf(readDto1, readDto2)

        every { subjectService.listAllSubjects() } returns storedSubjects

        val result = sut.allUsers()

        assertThat(result).containsAll(expectedSubjects)
    }

    @Test
    fun `allUsers - when a list of users is request and the service returns an empty list, an empty result must be returned`() {
        every { subjectService.listAllSubjects() } returns emptyList()

        val result = sut.allUsers()

        assertThat(result).isEmpty()
    }

    @Test
    fun `userById - when a specific user is requested, it must be returned`() {
        val testId = 77849
        val (expectedApiSubject, _, domainSubject) = configureSubjectModel { id = testId }

        every { subjectService.getSubject(testId) } returns domainSubject

        val result = sut.userById(testId)

        assertThat(result).isEqualTo(expectedApiSubject)
    }

    @Test
    fun `assignGroup - when a user group is assigned to a subject, the modified subject must be returned`() {
        val testId = 66948
        val (_, userGroupWriteDto, domainUserGroup) = configureUserGroupModel {}
        val (expectedApiSubject, _, domainSubject) = configureSubjectModel {
            id = testId; userGroups = setOf(domainUserGroup)
        }

        every { subjectService.assignGroupToSubject(testId, domainUserGroup) } returns domainSubject

        val result = sut.assignGroup(testId, userGroupWriteDto)

        assertThat(result).isEqualTo(expectedApiSubject)
    }

    @Test
    fun `delete - when a subject is deleted, a response with status code 204 must be returned`() {
        val testId = 99526
        every { subjectService.delete(testId) } just Runs

        val result = sut.delete(testId)

        verify { subjectService.delete(testId) }
        assertThat(result.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @Test
    fun `delete - when a subject is deleted but it doesn't exist, a response with status code 230 must be returned`() {
        val testId = 99526
        every { subjectService.delete(testId) } throws SubjectNotFoundException("")

        val result = sut.delete(testId)

        verify { subjectService.delete(testId) }
        assertThat(result.statusCodeValue).isEqualTo(230)
    }

    @Test
    fun `register - when a subject is registered, the newly persisted subject must be returned`() {
        val testId = 98655

        val (subjectReadDto, subjectWriteDto, domainSubject) = configureSubjectModel { id = testId }

        every { subjectService.register(any()) } returns domainSubject

        val result = sut.register(subjectWriteDto)

        assertThat(result.body).isEqualTo(subjectReadDto)
        assertThat(result.statusCode).isEqualTo(CREATED)
        // New Domain subjects are initialized with its id equal to -1
        verify { subjectService.register(eq(domainSubject.copy(id = -1))) }
    }

    @Test
    fun `login - when the user logs in and the authentication is successful, the http status 201 must be returned`() {
        val (_, subjectWriteDto, domainSubject) = configureSubjectModel {}

        every { subjectService.login(eq(domainSubject.copy(id = -1))) } returns true

        val result = sut.login(subjectWriteDto)

        assertThat(result.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @Test
    fun `login - when the user logs in and the authentication is successful, a valid JWT Token must be returned`() {
        val (_, subjectWriteDto, domainSubject) = configureSubjectModel {}

        every { subjectService.login(eq(domainSubject.copy(id = -1))) } returns true

        sut.login(subjectWriteDto)

        fail { "JWT is not yet implemented" }
    }

    @Test
    fun `login - when the user logs in and the authentication is fails, the http status 401 must be returned`() {
        val (_, subjectWriteDto, domainSubject) = configureSubjectModel {}

        every { subjectService.login(any()) } returns false

        val result = sut.login(subjectWriteDto)

        assertThat(result.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }
}
