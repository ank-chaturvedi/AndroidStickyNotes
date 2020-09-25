package com.basics.androidstickynotes.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.basics.androidstickynotes.database.NoteDatabase
import com.basics.androidstickynotes.model.Note

class NoteRepository(val application: Application) {

    var mData: MutableLiveData<List<Note>> = MutableLiveData()


    fun getAllNotes(): LiveData<List<Note>> {
        GetAllNotesAsyncTask(
            application
        ).execute(mData)
        return mData
    }


    fun insertNote(note: Note) {
        InsertNoteAsyncTask(
            application
        ).execute(note)
    }

    fun deleteNote(note: Note) {
        DeleteNoteAsyncTask(
            application
        ).execute(note)
    }

    fun deleteAll(){
        DeleteAllNoteAsyncTask(
            application
        ).execute()
    }

    fun updateNote(note: Note){
        UpdateNoteAsyncTask(
            application
        ).execute(note)
    }


    class GetAllNotesAsyncTask(val application: Application) :
        AsyncTask<MutableLiveData<List<Note>>, Void, Boolean>() {
        override fun doInBackground(vararg params: MutableLiveData<List<Note>>): Boolean {
            val db = Room.databaseBuilder(application, NoteDatabase::class.java, "note-db")
                .build()

            val liveData = params[0]

            liveData.postValue(db.notesDao().getAllNote())

            db.close()

            return true
        }

    }


    class InsertNoteAsyncTask(val application: Application) : AsyncTask<Note, Void, Boolean>() {
        override fun doInBackground(vararg params: Note): Boolean {
            val db = Room.databaseBuilder(application, NoteDatabase::class.java, "note-db")
                .build()

            val note = params[0]

            db.notesDao().insert(note)

            db.close()

            return true
        }

    }

    class UpdateNoteAsyncTask(val application: Application) : AsyncTask<Note, Void, Boolean>() {
        override fun doInBackground(vararg params: Note): Boolean {
            val db = Room.databaseBuilder(application, NoteDatabase::class.java, "note-db")
                .build()

            val note = params[0]

            db.notesDao().update(note)

            db.close()

            return true
        }

    }


    class DeleteNoteAsyncTask(val application: Application) : AsyncTask<Note, Void, Boolean>() {
        override fun doInBackground(vararg params: Note): Boolean {
            val db = Room.databaseBuilder(application, NoteDatabase::class.java, "note-db")
                .build()

            val note = params[0]

            db.notesDao().delete(note)

            db.close()

            return true
        }

    }

    class DeleteAllNoteAsyncTask(val application: Application) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void): Boolean {
            val db = Room.databaseBuilder(application, NoteDatabase::class.java, "note-db")
                .build()



            db.notesDao().deleteAll()

            db.close()

            return true
        }

    }


}