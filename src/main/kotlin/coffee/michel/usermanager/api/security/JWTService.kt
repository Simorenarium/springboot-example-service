package coffee.michel.usermanager.api.security

import com.nimbusds.jose.EncryptionMethod
import com.nimbusds.jose.JWEAlgorithm
import com.nimbusds.jose.JWEHeader
import com.nimbusds.jose.JWEObject
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.Payload
import com.nimbusds.jose.crypto.RSADecrypter
import com.nimbusds.jose.crypto.RSAEncrypter
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.KeyUse
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

@Service
internal class JWTService {

    // FIXME generating the Keys here will cause problems when scaling the application to multiple instances
    private final var senderPrivKey = RSAKeyGenerator(2048)
        .keyID("123")
        .keyUse(KeyUse.SIGNATURE)
        .generate()
    private final var senderPubKey = senderPrivKey.toPublicJWK()

    private final val recipientPrivKey: RSAKey = RSAKeyGenerator(2048)
        .keyID("456")
        .keyUse(KeyUse.ENCRYPTION)
        .generate()
    private final val recipientPubKey: RSAKey = recipientPrivKey.toPublicJWK()

    fun createJWT(sub: JWTSubject): String =
        createSignedJWT {
            subject("${sub.id}")
            claim("username", sub.username)
            claim("groups", Json.encodeToString(sub.groups))

            issueTime(Date())
            // 4 hours is quite random, this could be adjusted to a real use-case.
            // i.e. Full work days (8 hours)
            expirationTime(Date.from(Instant.now().plus(4, ChronoUnit.HOURS)))
        }
            .let { encrypt(it) }
            .serialize()

    fun parseJWT(jwt: String): JWTSubject =
        JWEObject.parse(jwt)
            .apply { decrypt(RSADecrypter(recipientPrivKey)) }
            .payload.toSignedJWT()
            .takeIf { it.verify(RSASSAVerifier(senderPubKey)) }
            ?.let {
                JWTSubject(
                    id = it.jwtClaimsSet.subject.toInt(),
                    username = it.jwtClaimsSet.getStringClaim("username"),
                    groups = Json.decodeFromString(it.jwtClaimsSet.getStringClaim("groups"))
                )
            }
            ?: throw Exception("Invalid JWT I guess")

    private fun createSignedJWT(block: JWTClaimsSet.Builder.() -> Unit) =
        SignedJWT(
            JWSHeader.Builder(JWSAlgorithm.RS256).keyID(senderPrivKey.keyID).build(),
            JWTClaimsSet.Builder().apply(block).build()
        ).apply { sign(RSASSASigner(recipientPrivKey)) }

    private fun encrypt(jwt: SignedJWT): JWEObject =
        JWEObject(
            JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM)
                .contentType("JWT") // required to indicate nested JWT
                .build(),
            Payload(jwt)
        ).apply { encrypt(RSAEncrypter(recipientPubKey)) }
}
