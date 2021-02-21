package com.ishant.dumboo.database.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EntityExample::class], version = 1, exportSchema = false)
abstract class AppDB : RoomDatabase() {
    abstract fun getDao(): DumbooDao
}