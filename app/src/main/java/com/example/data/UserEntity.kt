package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fullName: String,
    val phone: String,
    val email: String = "",
    val password: String,
    val memberTier: String = "VIP Member",
    val registeredAt: Long = System.currentTimeMillis()
)
