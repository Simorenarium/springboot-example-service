package coffee.michel.usermanager.api.controller

import coffee.michel.usermanager.UserGroupTestFixture.configureUserGroupModel
import coffee.michel.usermanager.domain.service.UserGroupService
import coffee.michel.usermanager.exception.UserGroupNotFoundException
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CREATED

class UserGroupControllerTest {

    private val userGroupService = mockk<UserGroupService>()

    private val sut = UserGroupController(userGroupService)

    @Test
    fun `allGroups - when a list of user groups is requested, they must be returned`() {
        val (readDto1, _, domainUserGroup1) = configureUserGroupModel { id = 1 }
        val (readDto2, _, domainUserGroup2) = configureUserGroupModel { id = 2 }
        val storedUserGroups = listOf(domainUserGroup1, domainUserGroup2)
        val expectedUserGroups = listOf(readDto1, readDto2)

        every { userGroupService.listAllUserGroups() } returns storedUserGroups

        val result = sut.allGroups()

        assertThat(result).containsAll(expectedUserGroups)
    }

    @Test
    fun `allGroups - when a list of user groups is request and the service returns an empty list, an empty result must be returned`() {
        every { userGroupService.listAllUserGroups() } returns emptyList()

        val result = sut.allGroups()

        assertThat(result).isEmpty()
    }

    @Test
    fun `userGroupById - when a specific user group is requested, it must be returned`() {
        val testId = 77849
        val (expectedApiGroup, _, domainGroup) = configureUserGroupModel { id = testId }

        every { userGroupService.getUserGroup(testId) } returns domainGroup

        val result = sut.userGroupById(testId)

        assertThat(result).isEqualTo(expectedApiGroup)
    }

    @Test
    fun `create - when user group is created, its persisted version must be returned`() {
        val (expectedApiGroup, _, domainGroup, _, name) = configureUserGroupModel {}

        every { userGroupService.createUserGroup(domainGroup.copy(id = -1)) } returns domainGroup

        val result = sut.create(name)

        assertThat(result.body).isEqualTo(expectedApiGroup)
        assertThat(result.statusCode).isEqualTo(CREATED)
    }

    @Test
    fun `delete - when a subject is deleted, a response with status code 204 must be returned`() {
        val testId = 99526
        every { userGroupService.deleteUserGroup(testId) } just Runs

        val result = sut.delete(testId)

        verify { userGroupService.deleteUserGroup(testId) }
        assertThat(result.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @Test
    fun `delete - when a subject is deleted but it doesn't exist, a response with status code 230 must be returned`() {
        val testId = 99526
        every { userGroupService.deleteUserGroup(testId) } throws UserGroupNotFoundException("")

        val result = sut.delete(testId)

        verify { userGroupService.deleteUserGroup(testId) }
        assertThat(result.statusCodeValue).isEqualTo(230)
    }
}
