package coffee.michel.usermanager.api

import coffee.michel.usermanager.SubjectTestFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SubjectWriteDtoTest {

    @Test
    fun `toString - hides password`() {
        val (_, subjectWriteDto) = SubjectTestFixture.configureSubjectModel {}

        val result = subjectWriteDto.toString()

        assertThat(result).contains(subjectWriteDto.username)
        assertThat(result).doesNotContain(subjectWriteDto.password)
    }
}
