package com.ishant.dumboo.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.ishant.dumboo.R
import com.ishant.dumboo.databinding.ActivityWelcomeBinding
import com.ishant.dumboo.ui.home.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity(),Animation.AnimationListener{
    lateinit var binding:ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_welcome)
        var animate=AnimationUtils.loadAnimation(this,R.anim.fade_in)
        binding.hello.animation=animate
        animate.setAnimationListener(this)

    }

    override fun onAnimationStart(animation: Animation?) {

    }

    override fun onAnimationEnd(animation: Animation?) {
        lifecycleScope.launch (Dispatchers.Main) {
            delay(1000L)
            startActivity(Intent(this@WelcomeActivity,HomeActivity::class.java))
            finish()
        }

    }

    override fun onAnimationRepeat(animation: Animation?) {
        TODO("Not yet implemented")
    }
}