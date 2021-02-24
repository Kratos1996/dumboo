package com.ishant.dumboo.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.ishant.dumboo.R
import com.ishant.dumboo.base.DumbooBaseActivity
import com.ishant.dumboo.databinding.ActivityLoginBinding
import com.ishant.dumboo.ui.welcome.WelcomeActivity
import io.reactivex.disposables.Disposable

class LoginActivity : DumbooBaseActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var activity:Context;
    private val viewModel:LoginViewModel by viewModels()
    private lateinit var disposable: Disposable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity=this
        binding=DataBindingUtil.setContentView(this,R.layout.activity_login)
        binding.sendCodeBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity,WelcomeActivity::class.java))
        } )
    }
}