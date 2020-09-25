package com.basics.androidstickynotes.viewmodel


import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.basics.androidstickynotes.model.Note
import com.basics.androidstickynotes.repository.NoteRepository

class NoteViewModel : ViewModel() {

     var repository: NoteRepository? = null
    var allNotes: LiveData<List<Note>>? = null

    fun init(application: Application){
        repository =
            NoteRepository(application)
        allNotes = repository!!.getAllNotes()
    }


    fun insert(note: Note) {
        repository!!.insertNote(note)
    }

    fun delete(note: Note) {
        repository!!.deleteNote(note)
    }

    fun getAllNote(): LiveData<List<Note>> {
        return allNotes!!
    }

    fun deleteAll(){
        repository!!.deleteAll()
    }

    fun updateNote(note: Note){
        repository!!.updateNote(note)
    }


}