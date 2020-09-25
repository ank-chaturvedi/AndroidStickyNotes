package com.basics.androidstickynotes.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.basics.androidstickynotes.model.Note


@Database(entities = [Note::class],version = 1)
abstract class NoteDatabase:RoomDatabase() {

      abstract fun notesDao(): NotesDao
}