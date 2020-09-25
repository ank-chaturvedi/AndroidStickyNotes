package com.basics.androidstickynotes.database

import androidx.room.*
import com.basics.androidstickynotes.model.Note


@Dao
interface NotesDao {

    @Insert
    fun insert(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("Select * From Note where id = :id")
    fun getNote(id:Int) : Note

    @Query("Select * From Note")
    fun getAllNote() :List<Note>

    @Query("Delete From Note")
    fun deleteAll()

    @Update
    fun update(note: Note)




}