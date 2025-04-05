package nishantpardamwar.notes_be

import nishantpardamwar.notes_be.models.Note
import nishantpardamwar.notes_be.models.NoteRes

fun Note.toNoteRes() = NoteRes(
    id = id.toHexString(), title = title, content = content, createdAt = createdAt.toEpochMilli()
)