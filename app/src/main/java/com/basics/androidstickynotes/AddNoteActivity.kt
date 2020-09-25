package com.basics.androidstickynotes

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.basics.androidstickynotes.dialog.ColorPickerDialog
import com.basics.androidstickynotes.model.Note
import kotlinx.android.synthetic.main.activity_add_note.*


class AddNoteActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)




        fbChooseColor.setOnClickListener {
            showColorDialog()
        }

        closeBtn.setOnClickListener {
            finish()
        }

        saveBtn.setOnClickListener {
            saveNote()


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


    private fun saveNote() {
        val title = titleEt.text.toString()
        val descrp = noteEt.text.toString()
        val color = contentRL.background as ColorDrawable
        val coloId = color.color


        if (title.isNullOrEmpty() || descrp.isNullOrEmpty()) {
            Toast.makeText(this, "Fields can't be empty", Toast.LENGTH_SHORT).show()
        } else {
            saveInDatabase(title, descrp, coloId)

        }
    }

    private fun saveInDatabase(title: String, descrp: String, colorId: Int) {
        val note = Note(
            title,
            descrp,
            colorId
        )
        titleEt.setText("")
        noteEt.setText("")
        val intent = Intent()
        intent.putExtra("title", note.title)
        intent.putExtra("color", colorId.toString())
        intent.putExtra("details", note.details)
        setResult(Activity.RESULT_OK,intent)
        finish()


    }


}
