package com.basics.androidstickynotes

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.basics.androidstickynotes.dialog.ColorPickerDialog
import com.basics.androidstickynotes.model.Note
import kotlinx.android.synthetic.main.activity_edit_note.closeBtn
import kotlinx.android.synthetic.main.activity_edit_note.contentRL
import kotlinx.android.synthetic.main.activity_edit_note.fbChooseColor
import kotlinx.android.synthetic.main.activity_edit_note.noteEt
import kotlinx.android.synthetic.main.activity_edit_note.saveBtn
import kotlinx.android.synthetic.main.activity_edit_note.titleEt

const val EDIT_NOTE_RESULT_CODE = 124
class EditNoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        val title = intent.getStringExtra("title")
        val details = intent.getStringExtra("details")
        val color = intent.getStringExtra("color")
        val i = intent.getStringExtra("id")
        val colorId = color!!.toInt()
        val id  = i!!.toLong()
        val note =
            Note(title, details, colorId, id)

        titleEt.setText(title)
        noteEt.setText(details)




        closeBtn.setOnClickListener {
            sendResult(false,note)
        }

        saveBtn.setOnClickListener {
            saveNote(id)
        }

        fbChooseColor.setOnClickListener {
            showColorDialog()
        }
    }


    private fun saveNote(id:Long) {
        val title = titleEt.text.toString()
        val descrp = noteEt.text.toString()
        val color = contentRL.background as ColorDrawable
        val colorId = color.color

        val note =
            Note(title, descrp, colorId, id)

        if (title.isNullOrEmpty() || descrp.isNullOrEmpty()) {
            Toast.makeText(this, "Fields can't be empty", Toast.LENGTH_SHORT).show()
        } else{
            sendResult(true,note)
        }
    }


    private fun showColorDialog() {
        val ft = supportFragmentManager.beginTransaction()
        val dialogFragment =
            ColorPickerDialog(this)
        dialogFragment.setOnClickListener(object : ColorPickerDialog.OnClickListener {
            override fun onClick(color: Int) {
                contentRL.setBackgroundColor(getColor(color))
            }

        })
        dialogFragment.show(ft, "dialog")
    }

    private fun sendResult(bool:Boolean,note: Note){
        val intent = Intent()
        intent.putExtra("result",bool)
        intent.putExtra("title", note.title)
        intent.putExtra("color", note.color.toString())
        intent.putExtra("details", note.details)
        intent.putExtra("id",note.id.toString())
        setResult(Activity.RESULT_OK,intent)
        finish()

    }
}
