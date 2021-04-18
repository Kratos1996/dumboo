package com.ishant.dumboo.repositories.methods

import android.app.Activity
import android.content.Context
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.ishant.dumboo.R
import com.ishant.dumboo.database.datastore.DataStoreBase
import com.ishant.dumboo.database.datastore.DataStoreCoroutinesHandler
import com.ishant.dumboo.ui.login.FirbaseAuthActions
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import javax.inject.Inject

class MethodsRepo {
    private lateinit var dataStore: DataStoreBase
    lateinit var ishant: String
    private lateinit var context: Context
    private lateinit var forceResendingTokenMAin: PhoneAuthProvider.ForceResendingToken
    private var verficationId = "0"
    private lateinit var mAuth: FirebaseAuth
private lateinit var authAction: FirbaseAuthActions;
    @Inject
    constructor(context: Context, dataStore: DataStoreBase) {
        this.context = context
        this.dataStore = dataStore
        mAuth = FirebaseAuth.getInstance()
    }

    fun MobAuth(activity: Activity, phoneNumber: String, authAction: FirbaseAuthActions) {
        this.authAction=authAction
        GlobalScope.launch(Dispatchers.IO) {
            withTimeout(60L) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+" + phoneNumber,
                    60L,
                    TimeUnit.SECONDS,
                    activity,
                    object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        override fun onCodeSent(
                            s: String,
                            forceResendingToken: PhoneAuthProvider.ForceResendingToken
                        ) {
                            super.onCodeSent(s, forceResendingToken)
                            verficationId = s
                            forceResendingTokenMAin = forceResendingToken
                            authAction.VerificationCodeSent()


                        }

                        override fun onCodeAutoRetrievalTimeOut(s: String) {
                            super.onCodeAutoRetrievalTimeOut(s)
                            authAction.TimeOut()

                        }

                        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                            signInWithPhoneAuthCredential(phoneAuthCredential, authAction)
                        }

                        override fun onVerificationFailed(s: FirebaseException) {
                            authAction.Error(s.message!!);
                        }
                    })
            }


        }

    }
    fun verifyVerificationCode(activity: Activity,code: String?,authAction: FirbaseAuthActions) {
        //creating the credential
        val credential = PhoneAuthProvider.getCredential(verficationId, code!!)

        //signing the user
        signInWithPhoneAuthCredential(activity, credential,authAction)
    }
     fun signInWithPhoneAuthCredential(activity: Activity,credential: PhoneAuthCredential,authAction: FirbaseAuthActions) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    authAction.startActivity(task.result.user!!.uid,task.result.user!!.phoneNumber)
                }else{
                    task.exception
                }

            }
    }

    fun fetchCharacterData(): String {
        return "Ishant"
    }

    private fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        authAction: FirbaseAuthActions
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            val data = async {
                mAuth.signInWithCredential(credential)
            }
            if (data.await().isSuccessful) {
                DataStoreCoroutinesHandler.io() {
                    dataStore.setUserId(data.await().result.user!!.uid)
                }
            } else if (data.await().isCanceled) {
                authAction.Error(context.getString(R.string.try_again))
            }
        }
    }

    fun isValidEmail(email: String?): Boolean {
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        return !Pattern.compile(EMAIL_PATTERN).matcher(email).matches()
    }

    fun isValidPhoneNumber(phone: String?): Boolean {
        val mobilePattern = "[0-9]{10}"
        return Pattern.matches(mobilePattern, phone)
    }

}