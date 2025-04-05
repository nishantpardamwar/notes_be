package nishantpardamwar.notes_be.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.Instant

data class User(@BsonId val id: ObjectId, val username: String, val hashedPassword: String)

data class Note(
    @BsonId val id: ObjectId = ObjectId.get(),
    val ownerId: ObjectId,
    val title: String,
    val content: String,
    val createdAt: Instant
)

sealed interface RegisterResult {
    data object UserAlreadyExist : RegisterResult
    data object Success : RegisterResult
}


sealed interface LoginResult {
    data object InvalidCred : LoginResult
    data class Success(val token: String) : LoginResult
}