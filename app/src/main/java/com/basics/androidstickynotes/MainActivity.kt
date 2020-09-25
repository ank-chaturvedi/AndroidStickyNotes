package com.basics.androidstickynotes


import android.app.Activity
import android.content.DialogInterface
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.basics.androidstickynotes.adapter.NoteAdapter
import com.basics.androidstickynotes.model.Note
import com.basics.androidstickynotes.profile.LoginActivity
import com.basics.androidstickynotes.viewmodel.NoteViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

const val ACTIVITY_CODE = 123

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var mAdapter: NoteAdapter
    var list: ArrayList<Note> = ArrayList()

    val db by lazy {
        FirebaseFirestore.getInstance()
    }
    val auth by lazy {
        FirebaseAuth.getInstance()
    }


    lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val mDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.close_drawer
        )

        drawerLayout.setDrawerListener(mDrawerToggle)
        mDrawerToggle.syncState()

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        noteViewModel.init(this.application)
        noteViewModel.getAllNote().observe(this, Observer {
            list.addAll(it)
            mAdapter.notifyDataSetChanged()

        })

        initRecyclerView()
        fb.setOnClickListener(this)

        navigationItemSelect()


    }


    private fun initRecyclerView() {
        notesRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mAdapter = NoteAdapter(this, list)
        notesRecyclerView.adapter = mAdapter

        mAdapter.setOnClickListener(object :
            NoteAdapter.OnClicklistener {
            override fun onDelete(note: Note) {

                deleteNote(note)
                Toast.makeText(this@MainActivity, "Item Deleted Successfully", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onEdit(note: Note) {
                val intent = Intent(this@MainActivity, EditNoteActivity::class.java)
                intent.putExtra("title", note.title)
                intent.putExtra("details", note.details)
                intent.putExtra("color", note.color.toString())
                intent.putExtra("id", note.id.toString())
                startActivityForResult(intent, EDIT_NOTE_RESULT_CODE)

                Toast.makeText(this@MainActivity, "item Edited successfully", Toast.LENGTH_SHORT)
                    .show()

            }

        })


    }

    private fun deleteNote(note: Note) {
        noteViewModel.delete(note)
        list.remove(note)
        mAdapter.notifyDataSetChanged()
    }

    override fun onClick(v: View) {

        when (v) {
            fb -> {
                openAddNotes()
            }
        }

    }

    private fun openAddNotes() {
        val intent = Intent(this, AddNoteActivity::class.java)
        startActivityForResult(intent, ACTIVITY_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Toast.makeText(this, "No Result occurred", Toast.LENGTH_SHORT).show()
                } else {
                    val title = data.getStringExtra("title")
                    val descrp = data.getStringExtra("details")
                    val color = data.getStringExtra("color").toInt()
                    val note = Note(
                        title,
                        descrp,
                        color
                    )
                    list.add(note)
                    noteViewModel.insert(note)
                    mAdapter.notifyDataSetChanged()
                }
            }
        }


        if (requestCode == EDIT_NOTE_RESULT_CODE && resultCode == Activity.RESULT_OK) {

            if (data == null) {
                Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show()
            } else {
                val title = data.getStringExtra("title")
                val descrp = data.getStringExtra("details")
                val color = data.getStringExtra("color").toInt()
                val id = data.getStringExtra("id").toLong()

                val note = Note(
                    title,
                    descrp,
                    color,
                    id
                )

                noteViewModel.updateNote(note)


                for (i in 0 until list.size) {
                    if (list[i].id == id) {
                        list[i] = note
                        mAdapter.notifyItemChanged(i)
                        break
                    }
                }
            }

        }
    }


    private fun navigationItemSelect() {
        nav.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.notes -> {

                }
                R.id.addNewNote -> {
                    openAddNotes()
                }
                R.id.syncNotes -> {

                    syncNotesWithFirebase()
                }
                R.id.rating -> {
                    Toast.makeText(this, "Soon this will add", Toast.LENGTH_SHORT).show()
                }
                R.id.shareApp -> {

                }

                R.id.logout -> {

                    logout()

                }

            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun logout() {

        val dialog = AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Please!Sync Before Logout to save data for Backup")
            .setPositiveButton("Logout") { _: DialogInterface, _: Int ->
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()

            }.setNegativeButton("cancel") { _: DialogInterface, _: Int ->

            }

        dialog.create()
        dialog.show()


    }

    private fun syncNotesWithFirebase() {
        val ref = db.collection("notes").document(auth.uid.toString())

        Toast.makeText(this, "Sync Started", Toast.LENGTH_SHORT).show()

        db.runBatch { batch ->


            for (value in list) {
                batch.set(ref.collection("note").document(value.hashCode().toString()), value)
            }
        }.addOnSuccessListener {
            Toast.makeText(this, "Synced Successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.DeleteAll -> {
                deleteAllNotes()
            }

            R.id.RestoreNotes -> {
                RestoreNotesFromFirestore()
            }


        }
        return super.onOptionsItemSelected(item)
    }


    private fun deleteAllNotes() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Delete All")
            .setMessage("Please!sync , if not then all data will be deleted")
            .setPositiveButton("Delete") { _: DialogInterface, _: Int ->
                noteViewModel.deleteAll()
                list.clear()
                mAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Notes Deleted Successfully!", Toast.LENGTH_SHORT).show()

            }.setNegativeButton("cancel") { _: DialogInterface, _: Int ->

            }

        dialog.create()
        dialog.show()






    }

    private fun RestoreNotesFromFirestore() {
        syncNotesWithFirebase()
        list.clear()
        val ref = db.collection("notes/${auth.uid.toString()}/note").get()

        noteViewModel.deleteAll()


        progressLayout.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE




        ref.addOnSuccessListener { documents ->
            for(document in documents){
                val data = document.data
                val title:String = data["title"].toString()
                val details:String = data["details"].toString()
                val color:Int = data["color"].toString().toInt()
                val id:Long = data["id"].toString().toLong()

                 val note = Note(title,details, color, id)
                list.add(note)
                noteViewModel.insert(note)

                Log.i("note",note.toString())
            }

            progressLayout.visibility = View.GONE
            progressBar.visibility = View.GONE

        }



    }


}




