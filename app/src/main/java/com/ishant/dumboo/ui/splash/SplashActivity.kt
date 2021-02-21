package com.ishant.dumboo.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.ishant.dumboo.R
import com.ishant.dumboo.animation.MyBounceInterpolator
import com.ishant.dumboo.base.DumbooBaseActivity
import com.ishant.dumboo.databinding.MainBinder
import com.ishant.dumboo.ui.home.HomeActivity
import kotlinx.coroutines.*
import java.util.*


class SplashActivity : DumbooBaseActivity() {
    private val mainAndroidViewModel: SplashViewModel by viewModels() //activityViewModels()

    companion object {
        private val TAG: String = SplashActivity::class.java.simpleName
        private lateinit var binding: MainBinder
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@SplashActivity, R.layout.activity_main)
        if (savedInstanceState == null) {
            binding.setAndroidViewModel(mainAndroidViewModel)
            binding.setLifecycleOwner(this@SplashActivity)
            observeAppName();
            mainAndroidViewModel.updateAppname(getString(R.string.app_name))
            val myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce_button)
            // Use bounce interpolator with amplitude 0.2 and frequency 20
            // Use bounce interpolator with amplitude 0.2 and frequency 20
            val interpolator = MyBounceInterpolator(0.2, 20.0)
            myAnim.interpolator = interpolator
            binding.logo.startAnimation(myAnim)

        }
      lifecycleScope.launch(Dispatchers.IO) {
//Dispatche IO is used when you call api or database saving process
            withContext(Dispatchers.Main) {
                if (isActive) {
                    swapActivty()
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    suspend fun swapActivty() {
        delay(3000L)
        startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
    }

    private fun observeAppName() {
        binding.androidViewModel?.getAppName()?.observe(this, object : Observer<String> {
            override fun onChanged(appName: String?) {
                if (appName != null && appName.isNotEmpty())
                    binding.textView.setText(appName)
            }

        })
    }



    }

