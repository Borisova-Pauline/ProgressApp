package com.tomli.progressapp.databases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "check_list", foreignKeys = [ForeignKey(entity = Scales::class, parentColumns = ["id"],
    childColumns = ["id_scale"], onDelete = ForeignKey.CASCADE)]
)
data class CheckList(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(defaultValue = "0") val id_scale: Int?,
    val done: Int?,
    val text: String?
)
