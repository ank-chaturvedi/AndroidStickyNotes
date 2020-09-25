package com.basics.androidstickynotes.model

import androidx.room.*
import com.basics.androidstickynotes.R


@Entity
data class Note(
    @ColumnInfo val title:String,
    @ColumnInfo val details:String,
    @ColumnInfo val color:Int = R.color.colorAccent,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo val id:Long = 0
)