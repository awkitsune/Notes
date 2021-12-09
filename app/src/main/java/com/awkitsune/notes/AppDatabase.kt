package com.awkitsune.notes

import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper

@Database(
    entities = [Note::class /*, AnotherEntityType.class, AThirdEntityType.class */],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao
}