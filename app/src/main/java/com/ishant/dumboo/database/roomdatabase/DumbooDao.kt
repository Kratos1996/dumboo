package com.ishant.dumboo.database.roomdatabase

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface DumbooDao {
    @Insert
    fun insert(example: EntityExample)
}