package com.momento.mystoic.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "Favourites", foreignKeys = [ForeignKey(
    entity = QuoteEntity::class,
    arrayOf("id"),
    arrayOf("id")
)])
data class FavouriteEntity (
    @PrimaryKey
    @NotNull
    val id: Int,
)