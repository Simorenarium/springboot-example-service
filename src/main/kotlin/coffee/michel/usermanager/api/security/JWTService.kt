package coffee.michel.usermanager.api.security

import coffee.michel.usermanager.api.SubjectReadDto
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.JWSVerifier
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.time.Duration
import java.time.Instant
import java.util.Date
import javax.naming.AuthenticationException

/**
 * This Service provides functions to create and parse JWT Tokens.
 * Aside from its primary purpose it also may sign, encrypt and validate any Token.
 *
 * I originally wanted to go for a combination of Signing and Encrypting,
 * but it was hard to debug and stole a lot of time (I've never done this before)
 *
 * @see <a href="https://connect2id.com/products/nimbus-jose-jwt/examples/signed-and-encrypted-jwt">Signed And Encrypted JWT<a/>
 * @see <a href="https://connect2id.com/products/nimbus-jose-jwt/examples/jwt-with-hmac">Simple JWT with HMAC "Protection"<a/>
 */
@Service
internal class JWTService {

    companion object {
        private val logger = KotlinLogging.logger { }
    }

    private final val signer: JWSSigner
    private final val verifier: JWSVerifier

    init {
        val rand = SecureRandom()
        val secret = ByteArray(32)
        rand.nextBytes(secret)

        logger.debug { "Generated secret for MAC Signing and Verification: ${String(secret)}" }

        signer = MACSigner(secret)
        verifier = MACVerifier(secret)
    }

    fun createJWT(sub: SubjectReadDto): String =
        createAndSignJWT {
            subject("${sub.sub}")
            claim("username", sub.username)
            claim("groups", Json.encodeToString(sub.groups))

            // TODO Replace with Provider
            issueTime(Date.from(Instant.now()))
            expirationTime(Date.from(Instant.now().plus(Duration.ofHours(4))))
        }
            .serialize()

    fun parseJWT(jwt: String): SubjectReadDto {
        val parsed: SignedJWT = parseAndVerifyJwt(jwt)
        return SubjectReadDto(
            sub = parsed.jwtClaimsSet.subject.toInt(),
            username = parsed.jwtClaimsSet.getStringClaim("username"),
            groups = Json.decodeFromString(parsed.jwtClaimsSet.getStringClaim("groups"))
        )
    }

    private fun createAndSignJWT(block: JWTClaimsSet.Builder.() -> Unit) =
        SignedJWT(
            createJWSHeader(),
            createClaimSet(block)
        ).apply { sign(signer) }

    private fun createClaimSet(block: JWTClaimsSet.Builder.() -> Unit): JWTClaimsSet =
        JWTClaimsSet.Builder().apply(block).build()

    private fun createJWSHeader() = JWSHeader.Builder(JWSAlgorithm.HS256).build()

    private fun parseAndVerifyJwt(jwt: String) =
        SignedJWT.parse(jwt)
            .takeIf { it.verify(verifier) }
            // TODO replace with own exception
            ?: throw AuthenticationException("")
}
