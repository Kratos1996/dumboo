package com.ishant.dumboo.database.prefrence

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData

/*
* Copyright (c) Ishant Sharma
* Android Developer
* ishant.sharma1947@gmail.com
* 7732993378
*/
class SharedPre private constructor(context: Context) {
    var mContext: Context
    private var totalIntake = 0


    var isLoggedIn: Boolean
        get() = GetDataBoolean(IS_LOGGED_IN)
        set(value) {
            SetDataBoolean(IS_LOGGED_IN, value)
        }

    fun isRegister(): Boolean {
        return GetDataBoolean(IS_REGISTER)
    }

    fun setIsregister(value: Boolean) {
        SetDataBoolean(IS_REGISTER, value)
    }

    fun setIsAppBackground(b: Boolean) {
        SetDataBoolean(APP_BACKGROUND, b)
    }

    fun IsAppBackgrounded(): Boolean {
        return GetDataBoolean(APP_BACKGROUND)
    }

    fun setUserId(uid: String) {
        SetDataString(USER_ID, uid)
    }

    val userId: String?
        get() = GetDataString(USER_ID)

    fun setUserMobile(userMobile: String) {
        SetDataString(MOBILE_NO, userMobile)
    }

    val userMobile: String?
        get() = GetDataString(MOBILE_NO)

    fun setName(name: String) {
        SetDataString(NAME, name)
    }

    val name: String?
        get() = GetDataString(NAME)


    fun setUserEmail(email: String) {
        SetDataString(EMAIL, email)
    }

    val userEmail: String?
        get() = GetDataString(EMAIL)


    var isNotificationMuted: Boolean
        get() = GetDataBoolean(NOTIFICATION_MUTED)
        set(notificationMuted) {
            SetDataBoolean(NOTIFICATION_MUTED, notificationMuted)
        }

    fun getConatctList(): String? {
        return GetDataString(FAV_CONTACT_LIST)
    }

    fun setConatctList(value: String?) {
        SetDataString(FAV_CONTACT_LIST, value!!)
    }
    private fun LogoutPrefrences() {
        removePreferences(NAME, mContext)
        removePreferences(EMAIL, mContext)
        removePreferences(MOBILE_NO, mContext)
        removePreferences(IS_LOGGED_IN, mContext)
        removePreferences(IS_REGISTER, mContext)
        removePreferences(USER_ID, mContext)
        removePreferences(NOTIFICATION_MUTED, mContext)
    }
    public  var totalIntakeMutable=MutableLiveData<Int>()
    public  var isWaterNotify=MutableLiveData<Boolean>()
    companion object {
        private const val ITI = "DUMBOO"
        private const val EMAIL = "email"
        private const val NAME = "name"
        private const val MOBILE_NO = "mobile_no"
        private const val APP_BACKGROUND = "app_in_background"
        private const val IS_LOGGED_IN = "login"
        private const val IS_REGISTER = "register"
        private const val USER_ID = "userId"
        private const val NOTIFICATION_MUTED = "notification_muted"
        private const val FAV_CONTACT_LIST = "contactList"


        private var Instance: SharedPre? = null


        @Synchronized
        fun getInstance(context: Context): SharedPre? {
            if (Instance == null) {
                Instance = SharedPre(context)
            }
            return Instance
        }

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(ITI, Context.MODE_PRIVATE)
        }

        private fun removePreferences(key: String, cntxt: Context) {
            getSharedPreferences(cntxt).edit().remove(key).commit()
        }
    }

    init {
        if (Instance != null) {
            throw RuntimeException("Use getInstance() method to get the single instance of this class( Mr.professional - Ishant ).")
        }
        mContext = context.applicationContext
    }

    //--------------------------------------Boolean Values--------------------------------------------
    //------------------------------------------------------------------------------------------------
    private fun GetDataString(key: String): String? {
        var cbValue: String? = null
        try {
            cbValue = getSharedPreferences(mContext).getString(key, "")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cbValue
    }

    private fun GetDataStringZero(key: String): String? {
        var cbValue: String? = null
        try {
            cbValue = getSharedPreferences(mContext).getString(key, "0.0")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cbValue
    }

    private fun SetDataString(key: String, value: String) {
        val edit = getSharedPreferences(mContext).edit()
        edit.putString(key, value)
        edit.commit()
    }

    private fun GetDataInt(key: String): Int {
        return getSharedPreferences(mContext).getInt(key, 0)
    }

    private fun SetDataInt(key: String, value: Int) {
        val edit = getSharedPreferences(mContext).edit()
        edit.putInt(key, value)
        edit.commit()
    }

    private fun GetDataLong(key: String): Long {
        return getSharedPreferences(mContext).getLong(key, 0)
    }

    private fun SetDataLong(key: String, value: Long) {
        val sp = getSharedPreferences(mContext)
        val edit = sp.edit()
        edit.putLong(key, value)
        edit.commit()
    }

    private fun GetDataBoolean(key: String): Boolean {
        val cbValue = getSharedPreferences(mContext).getBoolean(key, false)
        return cbValue
    }
    private fun GetDataBooleanTrue(key: String): Boolean {
        val cbValue = getSharedPreferences(mContext).getBoolean(key, true)
        return cbValue
    }

    private fun SetDataBoolean(key: String, value: Boolean) {
        val edit = getSharedPreferences(mContext).edit()
        edit.putBoolean(key, value)
        edit.commit()
    }

    fun Logout() {
        getSharedPreferences(mContext).edit().clear().commit()
        LogoutPrefrences()
    }


}