package com.tomli.progressapp.databases

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query("select * from themes")
    fun allThemes(): Flow<List<Themes>>

    @Query("insert into themes (name_theme, color) values (:name, :color)")
    suspend fun addTheme(name: String, color: String)


}