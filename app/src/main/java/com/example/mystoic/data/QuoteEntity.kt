package com.example.mystoic.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entity to represent the Quotes tables in the quotes database
@Entity(tableName = "Quotes")
data class QuoteEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val text: String?,
    val author: String?
)