package nishantpardamwar.notes_be.services.database

import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.flow.firstOrNull
import nishantpardamwar.notes_be.models.User

interface AuthDBService {
    suspend fun save(user: User)
    suspend fun getUserBy(username: String): User?
}

class AuthDBServiceImpl(
    private val dbClient: MongoClient
) : AuthDBService {
    private val userCollection = dbClient.getDatabase("noteDB").getCollection<User>("users")
    override suspend fun save(user: User) {
        userCollection.insertOne(user)
    }

    override suspend fun getUserBy(username: String): User? {
        return userCollection.find(
            eq(
                User::username.name, username
            )
        ).firstOrNull()
    }
}