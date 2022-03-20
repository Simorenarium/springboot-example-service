package coffee.michel.usermanager.persistence.storage

import coffee.michel.usermanager.UserGroupTestFixture.configureUserGroupModel
import coffee.michel.usermanager.exception.UserGroupNotFoundException
import coffee.michel.usermanager.persistence.repositories.UserGroupRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.Optional

class UserGroupJpaRepositoryStorageTest {

    private val userGroupRepository = mockk<UserGroupRepository>()

    private val sut = UserGroupJpaRepositoryStorage(userGroupRepository)

    @Test
    fun `list - when the repository returns a list of UserGroups, they must be returned`() {
        val (_, _, domainGroup1, entityGroup1) = configureUserGroupModel { id = 1 }
        val (_, _, domainGroup2, entityGroup2) = configureUserGroupModel { id = 2 }
        val storedUserGroups = listOf(entityGroup1, entityGroup2)
        val expectedUserGroups = listOf(domainGroup1, domainGroup2)

        every { userGroupRepository.findAll() } returns storedUserGroups

        val result = sut.list()

        assertThat(result).containsAll(expectedUserGroups)
    }

    @Test
    fun `list - when the repository returns an empty list of UserGroups, an empty list must be returned`() {
        every { userGroupRepository.findAll() } returns emptyList()

        val result = sut.list()

        assertThat(result).isEmpty()
    }

    @Test
    fun `get - when a UserGroup is requested, it must be returned`() {
        val testId = 99283
        val (_, _, domainGroup, entityGroup) = configureUserGroupModel { id = testId }

        every { userGroupRepository.findById(testId) } returns Optional.of(entityGroup)

        val result = sut.get(testId)

        assertThat(result).isEqualTo(domainGroup)
    }

    @Test
    fun `get - when a UserGroup is requested and not found, an exception must be thrown`() {
        val testId = 99283

        every { userGroupRepository.findById(testId) } returns Optional.empty()

        assertThrows<UserGroupNotFoundException>("UserGroup with id '$testId' not found.") { sut.get(testId) }
    }

    @Test
    fun `persist - when a UserGroup is persisted, it must return the persisted entity`() {
        val (_, _, domainGroup, entityGroup) = configureUserGroupModel { }
        every { userGroupRepository.save(entityGroup) } returns entityGroup

        val result = sut.persist(domainGroup)

        assertThat(result).isEqualTo(domainGroup)
    }

    @Test
    fun delete() {
        val testId = 99283
        val (_, _, _, entityGroup) = configureUserGroupModel { id = testId }

        every { userGroupRepository.findById(testId) } returns Optional.of(entityGroup)
        every { userGroupRepository.delete(entityGroup) } just Runs

        sut.delete(testId)

        verify { userGroupRepository.delete(entityGroup) }
    }

    @Test
    fun `delete - when a UserGroup should be deleted but was not found, an exception must be thrown`() {
        val testId = 99283

        every { userGroupRepository.findById(testId) } returns Optional.empty()

        assertThrows<UserGroupNotFoundException>("UserGroup with id '$testId' not found.") { sut.delete(testId) }
    }
}
