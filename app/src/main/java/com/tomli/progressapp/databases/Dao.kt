package com.tomli.progressapp.databases

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query("select * from themes")
    fun allThemes(): Flow<List<Themes>>
    @Query("select * from themes where id=:id")
    suspend fun oneTheme(id: Int): Themes
    @Query("insert into themes (name_theme, color) values (:name, :color)")
    suspend fun addTheme(name: String, color: String)
    @Query("delete from themes where id=:id")
    suspend fun deleteTheme(id: Int)
    @Query("update themes set name_theme=:name, color=:color where id=:id")
    suspend fun changeTheme(id: Int, name: String, color: String)

    @Query("select * from scales where id_theme=:id_theme")
    fun allScalesOneTheme(id_theme: Int?): Flow<List<Scales>>
    @Query("select id from scales where id_theme=:id_theme and type='Counter' and name_scale=:name and color=:color")
    suspend fun oneScaleCounterType(id_theme: Int, name: String, color: String): Int
    @Query("insert into scales (id_theme, name_scale, color, type) values (:id_theme, :name_scale, :color, :type)")
    suspend fun addScale(id_theme: Int, name_scale: String, color: String, type: String)
    @Query("delete from scales where id=:id")
    suspend fun deleteScale(id: Int)
    @Query("update scales set name_scale=:name_scale, color=:color where id=:id")
    suspend fun changeScale(id: Int, name_scale: String, color: String)

    @Query("select * from counter where id_scale=:id_scale")
    fun counterInsideScale(id_scale: Int): Flow<List<Counter>>
    @Query("insert into counter (id_scale, current_count, max_count) values (:id_scale, :current_count, :max_count)")
    suspend fun addCounter(id_scale: Int, current_count: Int, max_count: Int)
    @Query("update counter set current_count=:current_count where id=:id")
    suspend fun updateCurrentCount(id: Int, current_count: Int)
    @Query("update counter set max_count=:max_count where id=:id")
    suspend fun changeMaxCount(id: Int, max_count: Int)

    @Query("select * from check_list where id_scale=:id_scale")
    fun allCheckListOneScale(id_scale: Int): Flow<List<CheckList>>
    @Query("insert into check_list (id_scale, done, text) values (:id_scale, 0, :text)")
    suspend fun addCheckList(id_scale: Int, text: String)
    @Query("update check_list set done=:done where id=:id")
    suspend fun changeDoneCheckList(id: Int, done: Int)
    @Query("update check_list set text=:text where id=:id")
    suspend fun changeTextCheckList(id: Int, text: String)
    @Query("delete from check_list where id=:id")
    suspend fun deleteCheckList(id: Int)
}