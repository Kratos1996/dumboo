package com.ishant.dumboo.ui.login

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.ishant.dumboo.database.datastore.DataStoreBase
import com.ishant.dumboo.repositories.methods.MethodsRepo

class LoginViewModel : AndroidViewModel {
    private var datastore: DataStoreBase

    private var verficationId = "0"
    private lateinit var context: Context
    private lateinit var methods:MethodsRepo
    private lateinit var forceResendingTokenMAin: ForceResendingToken

    @ViewModelInject
    constructor (datastore: DataStoreBase, methods: MethodsRepo, application: Application) : super(Application()) {
        this.datastore = datastore
        this.context = application
        this.methods=methods
    }
    fun MobileAuthCheck(activity : Activity, phoneNumber: String,authActions: FirbaseAuthActions){
        methods.MobAuth(activity,phoneNumber,authActions);
    }

    fun verifyVerificationCode(code: String?, activityContext: Activity, firebaseActions: FirbaseAuthActions
    ) {
        methods.verifyVerificationCode(activityContext,code,firebaseActions)
    }




    fun ReSendOtp(activity : Activity, phoneNumber: String,authActions: FirbaseAuthActions) {
        methods.MobAuth(activity,phoneNumber,authActions);
    }
    fun getDataStore():DataStoreBase{
        return datastore!!;
    }

}