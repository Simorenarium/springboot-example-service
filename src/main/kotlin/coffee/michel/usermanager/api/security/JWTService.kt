package coffee.michel.usermanager.api.security

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.Payload
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import java.security.SecureRandom
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

    private final val secret = ByteArray(32).apply { SecureRandom().nextBytes(this) }
    private final val signer = MACSigner(secret)
    private final val verifier = MACVerifier(secret)

    fun createJWT(sub: JWTSubject): String =
        createAndSignJWT(sub).serialize()

    fun parseJWT(jwt: String): JWTSubject =
        Json.decodeFromString(parseAndVerifyJwt(jwt).payload.toString())

    private fun createAndSignJWT(entity: JWTSubject) =
        JWSObject(
            createJWSHeader(),
            mapToPayload(entity)
        ).apply { sign(signer) }

    private fun mapToPayload(entity: JWTSubject) =
        Payload(Json.encodeToString(entity))

    private fun createJWSHeader() = JWSHeader.Builder(JWSAlgorithm.HS256).build()

    private fun parseAndVerifyJwt(jwt: String) =
        JWSObject.parse(jwt)
            .takeIf { it.verify(verifier) }
            // TODO replace with own exception
            ?: throw AuthenticationException("")
}
