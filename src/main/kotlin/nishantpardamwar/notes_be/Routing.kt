package nishantpardamwar.notes_be

import nishantpardamwar.notes_be.models.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nishantpardamwar.notes_be.services.AuthService
import nishantpardamwar.notes_be.services.NoteService
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val authService by inject<AuthService>()
    val noteService by inject<NoteService>()
    routing {
        post("/register") {
            val req = call.receive<UserReq>()

            if (req.username.isBlank() || req.password.isBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            when (val result = authService.register(req.username, req.password)) {
                RegisterResult.Success -> call.respond(HttpStatusCode.OK)
                RegisterResult.UserAlreadyExist -> call.respond(HttpStatusCode.Conflict)
            }
        }

        post("/login") {
            val req = call.receive<UserReq>()
            when (val result = authService.login(req.username, req.password)) {
                LoginResult.InvalidCred -> call.respond(HttpStatusCode.Unauthorized)
                is LoginResult.Success -> call.respond(LoginRes(result.token))
            }
        }

        authenticate("auth-jwt") {
            get("/note/") {
                val userId = getAuthUserId()
                if (userId == null) {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@get
                }

                val notes = noteService.notesBy(userId).map { it.toNoteRes() }

                call.respond(notes)
            }

            get("/note/{id}") {
                val id = call.parameters["id"] ?: run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val userId = getAuthUserId()
                if (userId == null) {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@get
                }

                val note = noteService.getBy(userId, id)

                if (note != null) call.respond(note.toNoteRes())
                else call.respond(HttpStatusCode.NotFound)
            }

            post("/note/") {
                val note = call.receive<NoteAddReq>()

                val userId = getAuthUserId()
                if (userId == null) {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@post
                }

                val addedNote = noteService.addNote(userId, note)
                call.respond(addedNote.toNoteRes())
            }

            put("/note/") {
                val req = call.receive<NoteUpdateReq>()

                val userId = getAuthUserId()
                if (userId == null) {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@put
                }

                noteService.replace(userId, req)
                call.respond(HttpStatusCode.OK)
            }

            delete("/note/{id}") {
                val id = call.parameters["id"] ?: run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
                val userId = getAuthUserId()
                if (userId == null) {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@delete
                }

                noteService.deleteBy(userId, id)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

private fun RoutingContext.getAuthUserId(): String? {
    val principal = call.principal<JWTPrincipal>()
    return principal?.payload?.getClaim("userId")?.asString()
}
