package nishantpardamwar.notes_be.models

import kotlinx.serialization.Serializable

@Serializable
data class UserReq(val username: String, val password: String)

@Serializable
data class LoginRes(val token: String)

@Serializable
data class NoteAddReq(val title: String, val content: String)

@Serializable
data class NoteUpdateReq(val id: String, val title: String, val content: String)

@Serializable
data class NoteRes(
    val id: String, val title: String, val content: String, val createdAt: Long
)