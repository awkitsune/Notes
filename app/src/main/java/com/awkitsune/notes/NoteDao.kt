package com.awkitsune.notes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.util.*
import kotlin.collections.ArrayList

@Dao
interface NoteDao {
    @Insert
    fun insertAll(vararg note: Note)

    @Delete
    fun delete(note: Note)

    @Query("select * from note")
    fun getAllNotes(): List<Note>

    @Query("select * from note where date like :date")
    fun getAllNotesForDay(date: String): List<Note>
}