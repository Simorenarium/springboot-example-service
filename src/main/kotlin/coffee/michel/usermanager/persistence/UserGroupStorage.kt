package coffee.michel.usermanager.persistence

import coffee.michel.usermanager.domain.UserGroup
import coffee.michel.usermanager.exception.UserGroupNotFoundException

/**
 * Implementations if this Storage interface serve functions to manage all information in the persistence layer.
 */
internal interface UserGroupStorage {

    /**
     * Lists all UserGroups as they are stored.
     *
     * @return A list of UserGroups or an empty list if none are stored.
     */
    fun list(): List<UserGroup>

    /**
     * Reads a specific UserGroup from the Storage.
     *
     * @param id ID of the sought UserGroup
     * @return the sought UserGroup
     * @throws UserGroupNotFoundException if the sought UserGroup does not exist.
     */
    fun get(id: Int): UserGroup

    /**
     * Stores the given UserGroup.
     *
     * @return a modified UserGroup with a new ID.
     */
    fun persist(subject: UserGroup): UserGroup

    /**
     * Deletes the UserGroup with the given ID from the Storage.
     *
     * @throws UserGroupNotFoundException if the sought UserGroup does not exist.
     */
    fun delete(id: Int)
}
