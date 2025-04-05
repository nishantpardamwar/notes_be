package nishantpardamwar.notes_be.services.database

import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import nishantpardamwar.notes_be.models.Note
import org.bson.types.ObjectId

interface NoteDBService {
    suspend fun getNoteBy(ownerId: String, noteId: String): Note?
    suspend fun getNotesBy(ownerId: String): List<Note>
    suspend fun saveNote(note: Note): Note
    suspend fun replaceNote(note: Note): Note
    suspend fun deleteNote(ownerId: String, noteId: String)
}

class NoteDBServiceImpl(
    private val dbClient: MongoClient
) : NoteDBService {
    private val noteCollection = dbClient.getDatabase("noteDB").getCollection<Note>("notes")
    override suspend fun getNoteBy(ownerId: String, noteId: String): Note? {
        return noteCollection.find(
            and(
                eq(Note::ownerId.name, ObjectId(ownerId)), eq("_id", ObjectId(noteId))
            )
        ).firstOrNull()
    }

    override suspend fun getNotesBy(ownerId: String): List<Note> {
        return noteCollection.find(eq(Note::ownerId.name, ObjectId(ownerId))).toList()
    }

    override suspend fun saveNote(note: Note): Note {
        noteCollection.insertOne(note)
        return note
    }

    override suspend fun replaceNote(note: Note): Note {
        noteCollection.findOneAndReplace(
            and(
                eq(Note::ownerId.name, note.ownerId), eq("_id", note.id)
            ), note
        )
        return note
    }

    override suspend fun deleteNote(ownerId: String, noteId: String) {
        noteCollection.findOneAndDelete(
            and(
                eq(Note::ownerId.name, ObjectId(ownerId)), eq("_id", ObjectId(noteId))
            )
        )
    }

}