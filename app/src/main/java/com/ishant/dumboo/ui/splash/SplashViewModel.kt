package com.ishant.dumboo.ui.splash

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.ishant.dumboo.database.datastore.DataStoreBase
import com.ishant.dumboo.database.datastore.DataStoreCoroutinesHandler

class SplashViewModel @ViewModelInject constructor(private var datastore: DataStoreBase, application: Application) : AndroidViewModel(Application()) {
    fun getInstance() : String {
        return this.toString()
    }

    fun getRepositoryInstance() : String {
        return datastore.giveRepository()
    }

    fun update(booleanKey : Boolean) {
        DataStoreCoroutinesHandler.io(this@SplashViewModel) {
            datastore.update(booleanKey)
        }
    }

    fun update(stringKey : String) {
        DataStoreCoroutinesHandler.io(this@SplashViewModel) {
            datastore.update(stringKey)
        }
    }
    fun updateAppname(appName : String) {
        DataStoreCoroutinesHandler.io(this@SplashViewModel) {
            datastore.updateAppname(appName)
        }
    }

    fun update(integerKey : Int) {
        DataStoreCoroutinesHandler.io(this@SplashViewModel) {
            datastore.update(integerKey)
        }
    }

    fun update(doubleKey : Double) {
        DataStoreCoroutinesHandler.io(this@SplashViewModel) {
            datastore.update(doubleKey)
        }
    }

    fun update(longKey : Long) {
        DataStoreCoroutinesHandler.io(this@SplashViewModel) {
            datastore.update(longKey)
        }
    }

    fun observeBoolean() : LiveData<Boolean> {
        return datastore.getBoolean().asLiveData(/*viewModelScope.coroutineContext*/)
    }

    fun getAppName() : LiveData<String> {
        return datastore.getAppName().asLiveData(/*viewModelScope.coroutineContext*/)
    }

    fun observeInt() : LiveData<Int> {
        return datastore.getInteger().asLiveData(/*viewModelScope.coroutineContext*/)
    }

    fun observeDouble() : LiveData<Double> {
        return datastore.getDouble().asLiveData(/*viewModelScope.coroutineContext*/)
    }

    fun observeLong() : LiveData<Long> {
        return datastore.getLong().asLiveData(/*viewModelScope.coroutineContext*/)
    }
}