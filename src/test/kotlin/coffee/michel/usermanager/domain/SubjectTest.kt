package coffee.michel.usermanager.domain

import coffee.michel.usermanager.SubjectTestFixture.configureSubjectModel
import coffee.michel.usermanager.UserGroupTestFixture.configureUserGroupModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.security.core.authority.SimpleGrantedAuthority

internal class SubjectTest {

    @Test
    fun `toString - hides password`() {
        val (_, _, domainSubject) = configureSubjectModel {}

        val result = domainSubject.toString()

        assertThat(result).contains("${domainSubject.id}")
        assertThat(result).contains(domainSubject.username)
        assertThat(result).contains("${domainSubject.groups}")
        assertThat(result).doesNotContain(domainSubject.password)
    }

    @Nested
    @DisplayName("Matches UserDetails Specifications")
    inner class MatchesUserDetailSpec {

        @Test
        fun `getAuthorities - returns UserGroups mapped to Authorities`() {
            val (_, _, domainSubject) = configureSubjectModel {
                userGroups = setOf(
                    configureUserGroupModel { id = 1; name = "gr1" }.domain,
                    configureUserGroupModel { id = 2; name = "gr2" }.domain
                )
            }

            val result = domainSubject.authorities

            assertThat(result).containsExactlyInAnyOrder(
                SimpleGrantedAuthority("gr1"),
                SimpleGrantedAuthority("gr2")
            )
        }

        @Test
        fun `isAccountNonExpired - defaults to true`() {
            val (_, _, domainSubject) = configureSubjectModel {}

            val result = domainSubject.isAccountNonExpired

            assertThat(result).isTrue
        }

        @Test
        fun `isAccountNonLocked - defaults to true`() {
            val (_, _, domainSubject) = configureSubjectModel {}

            val result = domainSubject.isAccountNonLocked

            assertThat(result).isTrue
        }

        @Test
        fun `isCredentialsNonExpired - defaults to true`() {
            val (_, _, domainSubject) = configureSubjectModel {}

            val result = domainSubject.isCredentialsNonExpired

            assertThat(result).isTrue
        }

        @Test
        fun `isEnabled - defaults to true`() {
            val (_, _, domainSubject) = configureSubjectModel {}

            val result = domainSubject.isEnabled

            assertThat(result).isTrue
        }
    }
}
