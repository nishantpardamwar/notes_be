package nishantpardamwar.notes_be.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import nishantpardamwar.notes_be.audience
import nishantpardamwar.notes_be.issuer
import nishantpardamwar.notes_be.jwtSecret
import nishantpardamwar.notes_be.models.User
import java.util.*
import java.util.concurrent.TimeUnit

class JWTService {
    fun generateToken(user: User): String {
        val expiry = Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24))
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("userId", user.id.toHexString())
            .withExpiresAt(expiry)
            .sign(Algorithm.HMAC256(jwtSecret))
    }
}