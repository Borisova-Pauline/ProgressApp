package com.tomli.progressapp.databases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "scales", foreignKeys = [ForeignKey(entity = Themes::class, parentColumns = ["id"],
    childColumns = ["id_theme"], onDelete = ForeignKey.CASCADE)]
)
data class Scales(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(defaultValue = "0") val id_theme: Int?,
    val name_scale: String?,
    val color: String?,
    val type: String?
)
