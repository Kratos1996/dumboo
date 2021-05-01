package com.ishant.dumboo.database.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DumbooDao {
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insert(contact: ContactList)

    @Query("Select * From ContactList")
    fun GetContactList():LiveData<List<ContactList>>

    @Query("Delete From ContactList")
    fun DeleteAllContacts()
}