package com.momento.mystoic.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

// Entity to represent the Quotes tables in the quotes database
@Entity(tableName = "Quotes")
data class QuoteEntity (
    @NotNull
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @NotNull
    val text: String,
    @NotNull
    val author: String
)