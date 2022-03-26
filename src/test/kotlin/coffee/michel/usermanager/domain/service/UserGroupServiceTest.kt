package coffee.michel.usermanager.domain.service

import coffee.michel.usermanager.UserGroupTestFixture.configureUserGroupModel
import coffee.michel.usermanager.persistence.UserGroupStorage
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class UserGroupServiceTest {

    private val userGroupStorage = mockk<UserGroupStorage>()

    private val sut = UserGroupService(userGroupStorage)

    @Test
    fun `listAllUserGroups - when a list of all user groups is requested, it must be returned`() {
        val allUserGroups = listOf(
            configureUserGroupModel { id = 1 }.domain,
            configureUserGroupModel { id = 2 }.domain
        )

        every { userGroupStorage.list() } returns allUserGroups

        val result = sut.listAllUserGroups()

        assertThat(result).containsAll(allUserGroups)
    }

    @Test
    fun `listAllUserGroups - when a list of all user groups is requested but its empty, the empty result must be returned`() {
        every { userGroupStorage.list() } returns emptyList()

        val result = sut.listAllUserGroups()

        assertThat(result).isEmpty()
    }

    @Test
    fun `getUserGroup - when a specific user group is requested, it must be returned`() {
        val testId = 55756
        val (_, _, domainUserGroup) = configureUserGroupModel { id = testId }

        every { userGroupStorage.get(testId) } returns domainUserGroup

        val result = sut.getUserGroup(testId)

        assertThat(result).isEqualTo(domainUserGroup)
    }

    @Test
    fun `createUserGroup - when a user group is persisted, its persisted version must be returned`() {
        val (_, _, persistedDomainUserGroup) = configureUserGroupModel { }
        val domainUserGroup = persistedDomainUserGroup.copy(id = -1)

        every { userGroupStorage.persist(domainUserGroup) } returns persistedDomainUserGroup

        val result = sut.createUserGroup(domainUserGroup)

        assertThat(result).isEqualTo(persistedDomainUserGroup)
    }

    @Test
    fun `deleteUserGroup - when a user group is to be deleted, the delete must be performed`() {
        val testId = 55731

        every { userGroupStorage.delete(testId) } just Runs

        sut.deleteUserGroup(testId)

        verify { userGroupStorage.delete(testId) }
    }
}
