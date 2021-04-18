package com.ishant.dumboo.database.datastore

import kotlinx.coroutines.flow.Flow

interface DataStoreBase {

    fun giveRepository(): String

    suspend fun update(booleanKey: Boolean)

    suspend fun update(stringKey: String)

    suspend fun updateAppname(appName: String)

    suspend fun update(integerKey: Int)

    suspend fun update(doubleKey: Double)

    suspend fun update(longKey: Long)

    suspend fun setPhoneNumber(mobileNumber: String)

    suspend fun setCountryCode(countryCode : String)

    suspend fun setName(name:String)

    suspend fun setUserId(userId:String)

    fun getBoolean(): Flow<Boolean>

    fun getString(): Flow<String>

    fun getUserId(): Flow<String>

    fun getAppName(): Flow<String>

    fun getMobileNumber(): Flow<String>

    fun getName():Flow<String>

    fun getCountryCode(): Flow<String>

    fun getInteger(): Flow<Int>

    fun getDouble(): Flow<Double>

    fun getLong(): Flow<Long>
}