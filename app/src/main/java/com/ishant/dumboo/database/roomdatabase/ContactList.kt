package com.ishant.dumboo.database.roomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.PostalAddress
import org.jetbrains.annotations.NotNull

@Entity
data class ContactList(@PrimaryKey(autoGenerate = false)
                       var PhoneNumber:String,
                       @ColumnInfo var Name: String,
@ColumnInfo var isFav:Boolean)
