package com.tomli.progressapp.databases

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "themes")
data class Themes(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val name_theme: String?,
    val color: String?
)
