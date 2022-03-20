package coffee.michel.usermanager.domain

internal data class Subject(
    val id: Int,
    val username: String,
    val password: String,
    val groups: Set<UserGroup>
) {

    override fun toString(): String {
        return "Subject(id=$id, username='$username', password='<hidden>', groups=$groups)"
    }
}
