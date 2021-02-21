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

    fun getBoolean(): Flow<Boolean>

    fun getString(): Flow<String>

    fun getAppName(): Flow<String>

    fun getInteger(): Flow<Int>

    fun getDouble(): Flow<Double>

    fun getLong(): Flow<Long>
}