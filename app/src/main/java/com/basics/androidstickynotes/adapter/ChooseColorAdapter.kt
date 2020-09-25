package com.basics.androidstickynotes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.basics.androidstickynotes.R

class ChooseColorAdapter(private val context:Context, private val list:List<Int>):RecyclerView.Adapter<ChooseColorAdapter.ViewHolder>() {

    lateinit var listener: OnClickListener

    interface OnClickListener{
        fun onClick(color:Int)
    }

    fun setOnClickListener(listener: OnClickListener){
        this.listener = listener
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_choose_color,parent,false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount(): Int  = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var color = list[position]

        holder.cardView.setCardBackgroundColor(context.getColor(color))



        holder.cardView.setOnClickListener {
            listener.onClick(color)
        }
    }


    class ViewHolder(private val view:View):RecyclerView.ViewHolder(view){

        val cardView:CardView = view.findViewById(R.id.cardChooseColor)



    }


}