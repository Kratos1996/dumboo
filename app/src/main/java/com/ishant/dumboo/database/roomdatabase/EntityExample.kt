package com.ishant.dumboo.database.roomdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class EntityExample {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}