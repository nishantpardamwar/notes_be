package nishantpardamwar.notes_be

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mongodb.kotlin.client.coroutine.MongoClient
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.*
import kotlinx.serialization.json.Json
import nishantpardamwar.notes_be.services.*
import nishantpardamwar.notes_be.services.database.*
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun main(args: Array<String>) {
    embeddedServer(factory = Netty, module = Application::module)
}

fun Application.module() {
    install(CallLogging)
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "NoteBE"
            verifier(
                JWT.require(Algorithm.HMAC256(jwtSecret)).withAudience(audience).withIssuer(issuer).build()
            )
            validate { creds ->
                if (creds.payload.getClaim("userId").asString() != null) {
                    JWTPrincipal(creds.payload)
                } else null
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }


    startKoin {
        modules(
            module {
                single<MongoClient> { MongoClient.create(mongoDBUri) }
                single<AuthDBService> { AuthDBServiceImpl(get()) }
                single<NoteDBService> { NoteDBServiceImpl(get()) }
                single<EncoderService> { EncoderServiceImpl() }
                single<NoteService> { NoteService(get()) }
                single<AuthService> { AuthServiceImpl(get(), get(), get()) }
                single<JWTService> { JWTService() }
            }
        )
    }

    configureRouting()
}