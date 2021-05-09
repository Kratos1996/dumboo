package com.ishant.dumboo.ui.home

import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.ishant.dumboo.database.prefrence.SharedPre
import com.ishant.dumboo.database.roomdatabase.ContactList
import com.ishant.dumboo.database.roomdatabase.DatabaseRepository
import com.ishant.dumboo.ui.profile.model.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class HomeViewModel @ViewModelInject constructor(
    var sharedPre: SharedPre,
    var repository: DatabaseRepository
) : AndroidViewModel(Application()) {

    val document = Firebase.firestore.collection("Dumboo")
    val mutableUserData = MutableLiveData<UserData>()
    val error = MutableLiveData<String>()
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
                        val id =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                        if (hasPhoneNumber > 0) {
                            name = cursor.getString(nameIndex)
                            val phoneCursor: Cursor = activity.contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                arrayOf<String>(id),
                                null
                            )!!
                            if (phoneCursor.moveToNext()) {
                                val phoneNumber = phoneCursor.getString(
                                    phoneCursor.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER
                                    )
                                )
                                //number = deleteCountry(phoneNumber)
                                number = phoneNumber.replace("\\s+".toRegex(), "")

                                if (!mobileNoSet.contains(number)) {
                                    mobileNoSet.add(number)
                                    val data = repository.GetContact(number)
                                    if (data == null && number.length > 9) {
                                        var contact = ContactList(number, name, false)
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
                } finally {
                    cursor.close()
                }
            }


        }
    }

    var data: UserData = UserData()
    fun deleteCountry(phone: String): String {
        val phoneInstance = PhoneNumberUtil.getInstance()
        try {
            val phoneNumber = phoneInstance.parse(phone, "+91")
            return phoneNumber?.nationalNumber?.toString() ?: "0"
        } catch (_: Exception) {
        }
        return phone
    }

    fun GetUserDetail(): LiveData<UserData> {
        GlobalScope.launch {
            document.document(sharedPre.userId!!).get().addOnSuccessListener { task ->
              if(task!=null){
                  try{
                      data = task.toObject(UserData::class.java)!!
                      if (data != null) {
                          mutableUserData.postValue(data)
                      }
                  }catch (e:java.lang.Exception){

                  }

              }

            }
        }
        return mutableUserData
    }

    fun SetUserDetail(data: UserData): LiveData<String> {
        GlobalScope.launch{
            document.document(sharedPre.userId!!).set(data).await()
            val datam =
                document.document(sharedPre.userId!!).get().await().toObject(UserData::class.java)
            mutableUserData.postValue(datam!!)
            if (datam != null) {
                error.postValue("Profile Saved Successfully")
            } else {
                error.postValue("Error on Saving Profile")
            }

        }
        return error
    }

}