package com.ishant.dumboo.ui.home

import android.R.attr
import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.ishant.dumboo.base.DumbooBaseActivity
import com.ishant.dumboo.database.prefrence.SharedPre
import com.ishant.dumboo.database.roomdatabase.ContactList
import com.ishant.dumboo.database.roomdatabase.DatabaseRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HomeViewModel : AndroidViewModel {


    var repository: DatabaseRepository
    private var sharedPre: SharedPre

    @ViewModelInject
    constructor(sharedPre: SharedPre, repository: DatabaseRepository) : super(
        Application()
    ) {
        this.sharedPre = sharedPre
        this.repository = repository
    }

    fun LoadContact(activity: Activity) {
       GlobalScope.launch {
           val resolver: ContentResolver = activity.contentResolver
           val cursor = resolver.query(
               ContactsContract.Contacts.CONTENT_URI, null, null, null,
               null
           )

           if (cursor != null) {
               val mobileNoSet = HashSet<String>()
               try {
                   val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

                   var name: String
                   var number: String
                   while (cursor.moveToNext()) {
                       val hasPhoneNumber =
                           cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                               .toInt()
                       val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                       if (hasPhoneNumber > 0) {
                           name = cursor.getString(nameIndex)
                           val phoneCursor: Cursor = activity.contentResolver.query(
                               ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                               null,
                               ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                               arrayOf<String>(id),
                               null
                           )!!
                           if (phoneCursor != null) {
                               if (phoneCursor.moveToNext()) {
                                   val phoneNumber = phoneCursor.getString(
                                       phoneCursor.getColumnIndex(
                                           ContactsContract.CommonDataKinds.Phone.NUMBER
                                       )
                                   )
                                    number = deleteCountry(phoneNumber)
                                   if (!mobileNoSet.contains(number)) {
                                       mobileNoSet.add(number)
                                      val data= repository.GetContact(number)
                                       if(data==null && !number.equals("0")){
                                           var contact = ContactList(number, name,false)
                                           repository.InsertContact(contact)
                                       }
                                       Log.d(
                                           "hvy", "onCreaterrView  Phone Number: name = " + name
                                                   + " No = " + number
                                       )
                                   }

                                   //At here You can add phoneNUmber and Name to you listView ,ModelClass,Recyclerview
                                   phoneCursor.close()
                               }
                           }

                       }

                   }
               } finally {
                   cursor.close()
               }
       }


        }
    }
    fun deleteCountry(phone: String) : String{
        val phoneInstance = PhoneNumberUtil.getInstance()
        try {
            val phoneNumber = phoneInstance.parse(phone, "+91")
            return phoneNumber?.nationalNumber?.toString()?:"0"
        }catch (_ : Exception) {
        }
        return phone
    }
}