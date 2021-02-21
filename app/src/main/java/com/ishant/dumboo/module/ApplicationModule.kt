package com.ishant.dumboo.module
/*
* Android Developer
* Ishant Sharma
* Java and Kotlin
* */
import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.room.Room
import com.ishant.dumboo.database.datastore.DataStoreBase
import com.ishant.dumboo.database.datastore.DataStoreCustom
import com.ishant.dumboo.database.roomdatabase.AppDB
import com.ishant.dumboo.database.roomdatabase.DatabaseRepository
import com.ishant.dumboo.database.roomdatabase.DumbooDao
import com.ishant.dumboo.database.serverdatabase.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    public fun provideCustomRepository(dataStore  : DataStore<Preferences>) : DataStoreBase {
        return DataStoreCustom(dataStore)
    }

    @Provides
    public fun provideDataStore(@ApplicationContext context: Context) : DataStore<Preferences> {
        return context.createDataStore("Dumboo")
    }
    @Provides
    fun provideDumbooDatabase(@ApplicationContext context: Context): AppDB {
        return Room.databaseBuilder(context, AppDB::class.java,"DumbooDatabase").fallbackToDestructiveMigration().build()
    }

    @Provides
    fun providesPostDao(db: AppDB): DumbooDao = db.getDao()

    @Provides
    fun providesdatabaseRepository(@ApplicationContext context: Context,db: AppDB):DatabaseRepository =
        DatabaseRepository(db,context)
/*
    @Provides
    fun provideNetworkRepository(api:ApiService):NetworkRepository=NetworkRepository(api)*/

    /*
    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class ApplicationScope
    */
}