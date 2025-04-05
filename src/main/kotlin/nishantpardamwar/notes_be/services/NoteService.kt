package nishantpardamwar.notes_be.services

import nishantpardamwar.notes_be.models.Note
import nishantpardamwar.notes_be.models.NoteAddReq
import nishantpardamwar.notes_be.models.NoteUpdateReq
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import nishantpardamwar.notes_be.services.database.NoteDBService
import org.bson.types.ObjectId
import java.time.Instant

class NoteService(
    private val noteDBService: NoteDBService,
) {
    suspend fun getBy(userId: String, id: String): Note? {
        return noteDBService.getNoteBy(userId, id)
    }

    suspend fun notesBy(userId: String): List<Note> {
        return noteDBService.getNotesBy(userId)
    }

    suspend fun addNote(userId: String, note: NoteAddReq): Note {
        val note = Note(
            id = ObjectId(),
            ownerId = ObjectId(userId),
            title = note.title,
            content = note.content,
            createdAt = Instant.now()
        )
        return noteDBService.saveNote(note)
    }

    suspend fun replace(userId: String, req: NoteUpdateReq) {
        val note = Note(
            id = ObjectId(req.id),
            ownerId = ObjectId(userId),
            title = req.title,
            content = req.content,
            createdAt = Instant.now()
        )
        noteDBService.replaceNote(note)
    }

    suspend fun deleteBy(userid: String, id: String) {
        noteDBService.deleteNote(userid, id)
    }
}

