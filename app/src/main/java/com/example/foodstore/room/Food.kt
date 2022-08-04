package com.example.foodstore.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Table Food")
data class Food(

    // 1,2,3,4,5,6,7,8,9,10
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,

    val textSubject:String,
    val textPrice:String,
    val textDistance:String,
    val textCity:String,

    @ColumnInfo(name = "url")
    val urlImage:String,

    val numOfRating:Int,
    val rating:Float

)

