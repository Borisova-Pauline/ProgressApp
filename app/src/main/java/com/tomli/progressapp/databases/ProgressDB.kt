package com.tomli.progressapp.databases

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Themes::class], version = 1,
    exportSchema = true/*, autoMigrations = [AutoMigration(from=17, to=18)]*/)
abstract class ProgressDB : RoomDatabase() {
    abstract val dao: Dao
    companion object{
        fun createDB(context: Context): ProgressDB{
            return Room.databaseBuilder(context, ProgressDB::class.java, "progressDB.db")//.fallbackToDestructiveMigration()
                .createFromAsset("progressDB.db").build()
        }
    }
}