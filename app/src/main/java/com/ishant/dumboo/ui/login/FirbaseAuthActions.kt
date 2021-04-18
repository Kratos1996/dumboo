package com.ishant.dumboo.ui.login

interface FirbaseAuthActions {

    fun VerificationCodeSent()
    fun startActivity(uid: String, phoneNumber: String?)
    fun Error(message : String )
    fun TimeOut()
}