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

    fun setEmailProfile(googleProfile: String) {
        SetDataString(OUR_PROFILE, googleProfile)
    }

    val emailProfile: String?
        get() = GetDataString(OUR_PROFILE)

    fun setClientProfile(profileClient: String) {
        SetDataString(CLIENT_PROFILE, profileClient)
    }

    val clientProfile: String?
        get() = GetDataString(CLIENT_PROFILE)

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
    val notificationSound: String?
        get() = GetDataString(RINGTON_PATH)

    fun setNotificationSound(uri: String) {
        SetDataString(RINGTON_PATH, uri)
    }

    val notificationStatusKey: String?
        get() = GetDataString(NOTIFICATION_STATUS_KEY)

    fun setNotificationStatusKey(uri: String) {
        SetDataString(NOTIFICATION_STATUS_KEY, uri)
    }

    val notificationMessageKey: String?
        get() = GetDataString(NOTIFICATION_MSG_KEY)

    fun setNotificationMessageKey(uri: String) {
        SetDataString(NOTIFICATION_MSG_KEY, uri)
    }

    val NotificationFrequency: Int?
        get() = GetDataInt(NOTIFICATION_FREQUENCY_KEY)

    fun setNotificationFrequency(value: Int) {
        SetDataInt(NOTIFICATION_FREQUENCY_KEY, value)
    }

    fun getWeight(): Int {
        return GetDataInt(WEIGHT_KEY)
    }

    fun setWeight(value: Int) {
        return SetDataInt(WEIGHT_KEY, value)
    }

    fun getTotalIntake(): Int {

        return GetDataInt(TOTAL_INTAKE)
    }

    fun setTotalIntake(value: Int) {
        totalIntakeMutable.postValue(value)
        return SetDataInt(TOTAL_INTAKE, value)
    }

    fun getCurrentIntake(): Int {
        return GetDataInt(CURRENT_INTAKE)
    }

    fun setCurrentIntake(value: Int) {
        return SetDataInt(CURRENT_INTAKE, value)
    }

    fun getSleepingTime(): Long {
        return GetDataLong(SLEEPING_TIME_KEY)
    }

    fun setSleepingTime(value: Long) {
        return SetDataLong(SLEEPING_TIME_KEY, value)
    }

    fun getWakeUpTime(): Long {
        return GetDataLong(WAKEUP_TIME)
    }

    fun setWakeUpTime(value: Long) {
        return SetDataLong(WAKEUP_TIME, value)
    }

    fun isStopService(): Boolean {
        return GetDataBooleanTrue(STOP_SERVICE)
    }

    fun StopService(value: Boolean) {
        return SetDataBoolean(STOP_SERVICE, value)
    }

    fun isFirstRun(): Boolean {
        return GetDataBoolean(FIRST_RUN_KEY)
    }

    fun setFirstRun(vaue: Boolean) {
        return SetDataBoolean(FIRST_RUN_KEY, vaue)
    }

    fun setWorkTime(calue: Int) {
        SetDataInt(WORK_TIME_KEY, calue)
    }

    fun getWorkTime(): Int {
        return GetDataInt(WORK_TIME_KEY)
    }
    fun setWaterWorkerId(calue: String) {
        SetDataString(WATER_WORKER_ID, calue)
    }

    fun getWaterWorker(): String? {
        return GetDataString(WATER_WORKER_ID)
    }

    private fun LogoutPrefrences() {
        removePreferences(NAME, mContext)
        removePreferences(EMAIL, mContext)
        removePreferences(OUR_PROFILE, mContext)
        removePreferences(CLIENT_PROFILE, mContext)
        removePreferences(CLIENT_ID, mContext)
        removePreferences(MOBILE_NO, mContext)
        removePreferences(IS_LOGGED_IN, mContext)
        removePreferences(IS_REGISTER, mContext)
        removePreferences(USER_ID, mContext)
        removePreferences(FIREBASE_TOKEN, mContext)
        removePreferences(RINGTON_PATH, mContext)
        removePreferences(NOTIFICATION_MUTED, mContext)
    }
    public  var totalIntakeMutable=MutableLiveData<Int>()
    public  var isWaterNotify=MutableLiveData<Boolean>()
    companion object {
        private const val ITI = "DUMBOO"
        private const val EMAIL = "email"
        private const val NAME = "name"
        private const val OUR_PROFILE = "myProfileFromApi"
        private const val CLIENT_PROFILE = "profileFromFaceBook"
        private const val CLIENT_ID = "clientId"
        private const val MOBILE_NO = "mobile_no"
        private const val APP_BACKGROUND = "app_in_background"
        private const val IS_LOGGED_IN = "login"
        private const val IS_REGISTER = "register"
        private const val USER_ID = "userId"
        private const val FIREBASE_TOKEN = "firebaseToken"
        private const val RINGTON_PATH = "rington"
        private const val NOTIFICATION_MUTED = "notification_muted"
        private const val STOP_SERVICE = "StopService"
        private const val WEIGHT_KEY = "weight"
        private const val WORK_TIME_KEY = "worktime"
        private const val TOTAL_INTAKE = "totalintake"
        private const val CURRENT_INTAKE = "currentIntake"
        private const val NOTIFICATION_STATUS_KEY = "notificationstatus"
        private const val NOTIFICATION_FREQUENCY_KEY = "notificationfrequency"
        private const val NOTIFICATION_MSG_KEY = "notificationmsg"
        private const val SLEEPING_TIME_KEY = "sleepingtime"
        private const val WAKEUP_TIME = "wakeuptime"
        private const val NOTIFICATION_TONE_URI_KEY = "notificationtone"
        private const val FIRST_RUN_KEY = "firstrun"
        private const val WATER_DATA_OF_THE_DAY = "date_of_water Data add"
        private const val WATER_WORKER_ID = "water_worker_id"
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

    fun setDailyDate(dateNow: String) {
        SetDataString(WATER_DATA_OF_THE_DAY, dateNow)
    }

    fun getWaterDate(): String? {
        return GetDataString(WATER_DATA_OF_THE_DAY)
    }


}