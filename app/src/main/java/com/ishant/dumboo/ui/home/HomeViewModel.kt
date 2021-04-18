package com.ishant.dumboo.ui.home

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.ishant.dumboo.database.prefrence.SharedPre
import com.ishant.dumboo.database.roomdatabase.DatabaseRepository

class HomeViewModel : AndroidViewModel {
    private var repository: DatabaseRepository
    private var sharedPre: SharedPre

    @ViewModelInject
    constructor(sharedPre: SharedPre, repository: DatabaseRepository) : super(
        Application()
    ) {
        this.sharedPre=sharedPre
        this.repository=repository
    }
}