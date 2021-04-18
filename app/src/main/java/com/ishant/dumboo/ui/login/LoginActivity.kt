package com.ishant.dumboo.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.ishant.dumboo.R
import com.ishant.dumboo.base.DumbooBaseActivity
import com.ishant.dumboo.database.datastore.DataStoreCoroutinesHandler
import com.ishant.dumboo.databinding.ActivityLoginBinding
import com.ishant.dumboo.ui.home.HomeActivity
import io.reactivex.disposables.Disposable
import java.util.*

class LoginActivity : DumbooBaseActivity(), FirbaseAuthActions {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var activity: Context;
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var disposable: Disposable
    lateinit var activityContext: Activity;
    lateinit var firebaseActions: FirbaseAuthActions
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        activityContext = this
        firebaseActions = this
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.ccp.ccpDialogShowTitle = true
        binding.sendCodeBtn.setOnClickListener(View.OnClickListener {
            // startActivity(Intent(activity,WelcomeActivity::class.java))
            binding.progressBar.visibility = View.VISIBLE
            binding.sendCodeTxt.visibility = View.GONE
            if (binding.etPhone.text != null && binding.etPhone.text.isNotEmpty() && getUtils().isValidPhoneNumber(
                    binding.etPhone.text.toString()
                )
            ) {
                viewModel.MobileAuthCheck(
                    activityContext,
                    binding.ccp.selectedCountryCode + binding.etPhone.text.toString(),
                    firebaseActions
                )
            } else {
                showCustomAlert("Please Enter Valid Number", binding.root)
            }
        })
        binding.cvVerify.setOnClickListener {
            if( !binding.otpView.otp.toString().isNullOrEmpty()){
                viewModel.verifyVerificationCode(
                    binding.otpView.otp,
                    activityContext!!,
                    firebaseActions
                )
            }else{
                showCustomAlert("Please Enter OTP",binding.root)
            }

        }
        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val v = String.format("%02d", millisUntilFinished / 60000)
                val va = (millisUntilFinished % 60000 / 1000).toInt()
                binding.tvDetectingOtpTime.text =
                    "Detecting OTP : " + v + ":" + String.format("%02d", va)
                binding.tvResendOtp.visibility = View.GONE
            }

            override fun onFinish() {
                binding.tvResendOtp.visibility = View.VISIBLE
            }
        }
        binding.etPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.length == 10 && getUtils().isValidPhoneNumber(s.toString())) {
                    DataStoreCoroutinesHandler.io(viewModel) {
                        viewModel.getDataStore().setPhoneNumber(s.toString())
                    }
                }
            }

        })
        viewModel.getDataStore().getMobileNumber().asLiveData()
            .observe(this, object : Observer<String> {
                override fun onChanged(mobileNumber: String?) {
                    /* if (mobileNumber != null && mobileNumber.isNotEmpty()) {
                        viewModel.MobileAuthCheck(activityContext, mobileNumber, firebaseActions)
                    }*/
                }
            })
    }

    override fun onIncomingCallReceived(ctx: Context?, number: String?, start: Date?) {

    }

    override fun onIncomingCallAnswered(ctx: Context?, number: String?, start: Date?) {

    }

    override fun onIncomingCallEnded(ctx: Context?, number: String?, start: Date?, end: Date?) {

    }

    override fun onOutgoingCallStarted(ctx: Context?, number: String?, start: Date?) {

    }

    override fun onOutgoingCallEnded(ctx: Context?, number: String?, start: Date?, end: Date?) {

    }

    override fun onMissedCall(context: Context?, savedNumber: String?, callStartTime: Date?) {

    }

    override fun RingType(ringerMode: Int) {

    }

    override fun VerificationCodeSent() {
        binding.progressBar.visibility = View.GONE
        binding.sendCodeTxt.visibility = View.GONE
        binding.sendCodeBtn.visibility = View.GONE
        binding.lytEnterOtp.visibility = View.VISIBLE
        timer?.start()
    }

    override fun startActivity(uid: String, phoneNumber: String?) {
        DataStoreCoroutinesHandler.io(viewModel) {
            viewModel.getDataStore().setPhoneNumber(phoneNumber!!)
            viewModel.getDataStore().setUserId(uid!!)
            sharedPre.setUserId(uid)
            sharedPre.setUserMobile(phoneNumber)
            sharedPre.isLoggedIn = true
        }
        startActivity(Intent(activityContext, HomeActivity::class.java))
    }

    override fun Error(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.sendCodeTxt.visibility = View.VISIBLE
        binding.sendCodeBtn.visibility = View.VISIBLE
        binding.lytEnterOtp.visibility = View.GONE
    }

    override fun TimeOut() {
        binding.progressBar.visibility = View.GONE
        binding.sendCodeTxt.visibility = View.GONE
        binding.sendCodeBtn.visibility = View.GONE
        binding.lytEnterOtp.visibility = View.VISIBLE
        // timer?.start()
    }
}


