package com.basics.androidstickynotes.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basics.androidstickynotes.adapter.ChooseColorAdapter
import com.basics.androidstickynotes.R

class ColorPickerDialog(private val cont:Context):DialogFragment() {

     private val colorList = listOf<Int>(
         R.color.amber,
         R.color.blue,
         R.color.pink,
         R.color.purple,
         R.color.amber,
         R.color.orange,
         R.color.yellow
     )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE,
            R.style.Theme_Dialog
        )
    }

   lateinit var listener: OnClickListener

    interface OnClickListener{
        fun onClick(color:Int)
    }

    fun setOnClickListener(listener: OnClickListener){
        this.listener = listener
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_color_picker_dialog,container,false)
        val recycler:RecyclerView = view.findViewById(R.id.chooseColorRecycler)
        recycler.layoutManager = GridLayoutManager(cont,5)
        val adapter =
            ChooseColorAdapter(
                cont,
                colorList
            )
        adapter.setOnClickListener(object :
            ChooseColorAdapter.OnClickListener {
            override fun onClick(color: Int) {
                listener.onClick(color)
            }

        })
        recycler.adapter = adapter


        return view
    }
}