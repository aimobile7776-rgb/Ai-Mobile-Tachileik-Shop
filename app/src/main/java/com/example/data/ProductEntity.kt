package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class FavouriteProduct(
    @PrimaryKey val id: String,
    val name: String,
    val price: Long,
    val condition: String,
    val category: String,
    val specs: String,
    val addedTime: Long = System.currentTimeMillis()
)
