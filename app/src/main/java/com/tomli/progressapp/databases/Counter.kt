package com.tomli.progressapp.databases

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "counter", foreignKeys = [ForeignKey(entity = Scales::class, parentColumns = ["id"],
    childColumns = ["id_scale"], onDelete = ForeignKey.CASCADE)]
)
data class Counter(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(defaultValue = "0") val id_scale: Int?,
    val current_count: Int?,
    val max_count: Int?
)
