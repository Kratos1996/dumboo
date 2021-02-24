package com.ishant.dumboo.ui.login

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import com.ishant.dumboo.database.datastore.DataStoreBase

class LoginViewModel : AndroidViewModel {
    private var datastore: DataStoreBase

    @ViewModelInject
    constructor (datastore: DataStoreBase, application: Application):super(Application()){
        this.datastore = datastore
    }
}