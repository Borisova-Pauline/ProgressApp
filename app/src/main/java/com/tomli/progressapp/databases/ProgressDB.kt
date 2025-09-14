package com.tomli.progressapp.databases

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Themes::class, Scales::class, Counter::class, CheckList::class], version = 4,
    exportSchema = true, autoMigrations = [AutoMigration(from=1, to=2), AutoMigration(from=2, to=3),
        AutoMigration(from=3, to=4)])
abstract class ProgressDB : RoomDatabase() {
    abstract val dao: Dao
    companion object{
        fun createDB(context: Context): ProgressDB{
            return Room.databaseBuilder(context, ProgressDB::class.java, "progressDB.db")//.fallbackToDestructiveMigration()
                .createFromAsset("progressDB.db").build()
        }
    }
}