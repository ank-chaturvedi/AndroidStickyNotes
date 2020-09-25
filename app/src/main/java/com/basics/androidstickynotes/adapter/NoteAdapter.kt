package com.basics.androidstickynotes.adapter


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity

import androidx.recyclerview.widget.RecyclerView
import com.basics.androidstickynotes.model.Note
import com.basics.androidstickynotes.R

class NoteAdapter(val context:Context,val list:List<Note>) :RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(){



    lateinit var listener: OnClicklistener

    interface OnClicklistener{
        fun onDelete(note: Note)
        fun onEdit(note: Note)
    }

    fun setOnClickListener(listener: OnClicklistener){
        this.listener = listener
    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_note,parent,false)
        return NoteViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = list.size



    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note  = list[position]
        val tit = list[position].title
        val color = '#' + Integer.toHexString(list[position].color)
        val descrip = list[position].details.trim()



        holder.title.text = tit
        holder.description.apply {
            text = descrip
        }

        holder.descLayout.setBackgroundColor(Color.parseColor(color))

        holder.more.setOnClickListener {

            val popUp = PopupMenu(context,holder.view)
            val menuInflater = popUp.menuInflater
            popUp.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.delete ->{
                        listener.onDelete(note)
                         true
                    }
                    R.id.edit ->{
                        listener.onEdit(note)
                        true
                    }
                    R.id.share ->{
                        share(list[position])
                        true
                    }
                    else -> false
                }

            }
            menuInflater.inflate(R.menu.more_menu,popUp.menu)
            popUp.show()
        }


    }

    private fun share(note: Note) {

        val sharingIntent = Intent(Intent.ACTION_SEND)

        sharingIntent.setType("text/plain")
        sharingIntent.putExtra(Intent.EXTRA_TEXT,note.toString())
        context.startActivity(Intent.createChooser(sharingIntent,"Send Via"))

    }


    class NoteViewHolder(val view: View):RecyclerView.ViewHolder(view){
        val title:TextView = view.findViewById(R.id.titleTv)
        val description:TextView = view.findViewById(R.id.descriptionTv)
        val more:ImageView = view.findViewById(R.id.moreImg)
        val descLayout : LinearLayout = view.findViewById(R.id.descLayout)
    }


}


