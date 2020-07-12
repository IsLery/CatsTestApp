package com.islery.catstestapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.islery.catstestapp.data.db.CATS_TABLE


@Entity(tableName = CATS_TABLE)
data class Cat(
    @PrimaryKey @field:SerializedName("id") val id: String,
    @field:SerializedName("url") val url: String,
    var isFav: Boolean = false
)

