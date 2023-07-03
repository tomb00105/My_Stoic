package com.example.mystoic.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "Journal")
data class JournalEntity (
    @PrimaryKey(autoGenerate = false)
    @NotNull
    var date: String,
    val text: String?,
)