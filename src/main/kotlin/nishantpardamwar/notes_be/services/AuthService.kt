package nishantpardamwar.notes_be.services

import nishantpardamwar.notes_be.models.LoginResult
import nishantpardamwar.notes_be.models.RegisterResult
import nishantpardamwar.notes_be.models.User
import nishantpardamwar.notes_be.services.database.AuthDBService
import org.bson.types.ObjectId

interface AuthService {
    suspend fun register(username: String, password: String): RegisterResult
    suspend fun login(username: String, password: String): LoginResult
}

class AuthServiceImpl(
    private val authDBService: AuthDBService,
    private val encoder: EncoderService,
    private val jwtService: JWTService
) : AuthService {

    override suspend fun register(username: String, password: String): RegisterResult {
        val existingUser = authDBService.getUserBy(username)

        if (existingUser != null) return RegisterResult.UserAlreadyExist

        val hashedPassword = encoder.hash(password)
        val user = User(id = ObjectId.get(), username = username, hashedPassword = hashedPassword)

        authDBService.save(user)
        return RegisterResult.Success
    }

    override suspend fun login(username: String, password: String): LoginResult {
        val user = authDBService.getUserBy(username) ?: return LoginResult.InvalidCred

        return if (encoder.verify(password, user.hashedPassword)) {
            LoginResult.Success(jwtService.generateToken(user))
        } else {
            LoginResult.InvalidCred
        }
    }
}