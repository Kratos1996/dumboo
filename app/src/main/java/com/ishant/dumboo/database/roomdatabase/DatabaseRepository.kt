package com.ishant.dumboo.database.roomdatabase

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class DatabaseRepository @Inject constructor(val db: AppDB, val context: Context) {

    fun InsertContact(contact: ContactList) {
        GlobalScope.launch {
            db.getDao().insert(contact)
        }
    }

    suspend fun getUserFromDB():List<ContactList> {
        val data = db.getDao().GetFavContactList(true)
        return  data

    }
    fun GetContact(phone:String):ContactList{

        return db.getDao().GetPhone(phone)
    }
    suspend fun GetContact(phone:String,isFav: Boolean):ContactList{
        return db.getDao().GetPhone(phone,isFav)
    }
    fun SetFavContact( phone:String,isFav:Boolean){
        GlobalScope.launch {
            val int = db.getDao().setFavPhone(phone, isFav)
             val data=int
        }
    }
    fun DeleteAll() {
        GlobalScope.launch {
            db.getDao().DeleteAllContacts()
        }
    }
    fun GetContactList():LiveData<List<ContactList>>{
        return db.getDao().GetContactList()
    }
    fun GetContactList(data:String):LiveData<List<ContactList>>{
        return db.getDao().GetContactList(data)
    }

    fun GetFavList():LiveData<List<ContactList>>{
        return db.getDao().GetFavContactListLive(true)
    }
    fun GetFavList(data:String):LiveData<List<ContactList>>{
        return db.getDao().GetFavContactListLive(data,true)
    }
}