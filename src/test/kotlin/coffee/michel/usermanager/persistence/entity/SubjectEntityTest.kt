package coffee.michel.usermanager.persistence.entity

import coffee.michel.usermanager.SubjectTestFixture.configureSubjectModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SubjectEntityTest {

    @Test
    fun `toString - hides password`() {
        val (_, _, _, entitySubject) = configureSubjectModel {}

        val result = entitySubject.toString()

        assertThat(result).contains("${entitySubject.id}")
        assertThat(result).contains(entitySubject.username)
        assertThat(result).contains("${entitySubject.groups}")
        assertThat(result).doesNotContain(entitySubject.password)
    }
}
