package com.ishant.dumboo.database.roomdatabase

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DatabaseRepository @Inject constructor(val db: AppDB, val context: Context) {

    fun InsertContact(contact: ContactList) {
        GlobalScope.launch {
            db.getDao().insert(contact)
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
}