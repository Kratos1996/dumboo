package com.ishant.dumboo.database.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DumbooDao {
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    fun insert(contact: ContactList)

    @Query("Select * From ContactList")
    fun GetContactList():LiveData<List<ContactList>>

    @Query("Select * From ContactList where Name Like '%' || :data || '%' ")
    fun GetContactList(data:String ):LiveData<List<ContactList>>

    @Query("Select * From ContactList where Name Like '%' || :data || '%' and isFav=:isFav ")
    fun GetFavContactListLive(data:String,isFav:Boolean ):LiveData<List<ContactList>>

    @Query("Delete From ContactList")
    fun DeleteAllContacts()

    @Query("Select * From ContactList where PhoneNumber=:phone")
    fun GetPhone(phone:String):ContactList

    @Query("Select * From ContactList where PhoneNumber=:phone and isFav=:isFav")
    fun GetPhone(phone:String,isFav: Boolean):ContactList


    @Query("Update ContactList set isFav=:isFav Where PhoneNumber=:phone")
    fun setFavPhone(phone:String,isFav:Boolean):Int

    @Query("Select * From ContactList Where isFav=:isFav")
     fun GetFavContactList(isFav:Boolean):List<ContactList>

    @Query("Select * From ContactList Where isFav=:isFav")
    fun GetFavContactListLive(isFav:Boolean):LiveData<List<ContactList>>

}