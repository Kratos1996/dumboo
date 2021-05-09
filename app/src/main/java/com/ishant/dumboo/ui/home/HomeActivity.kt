package com.ishant.dumboo.ui.home

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.ishant.dumboo.R
import com.ishant.dumboo.base.DumbooBaseActivity
import com.ishant.dumboo.databinding.ActivityHomeBinding
import com.ishant.dumboo.ui.contact.ContactFragment
import com.ishant.dumboo.ui.setting.SettingFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class HomeActivity : DumbooBaseActivity(), NavigationView.OnNavigationItemSelectedListener {
private lateinit var binding:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_home)
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected)
        startFragment(HomeFragment.newInstance(viewmodel), HomeFragment.toString(), true)
    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bottomnav_home -> {
                if (getCurrentFragment() !is HomeFragment)
                    startFragment(HomeFragment.newInstance(viewmodel), true, HomeFragment.toString())
            }
            R.id.contact -> {
                if (getCurrentFragment() !is ContactFragment)
                    startFragment(ContactFragment.newInstance(viewmodel), true, ContactFragment.toString())
            }
            R.id.setting ->{
                if(getCurrentFragment()!is SettingFragment)
                    startFragment(SettingFragment.newInstance(viewmodel!!), true, SettingFragment.toString())
            }

            else -> {
                showCustomAlert("Menu Not Found", binding.root)
            }
        }
        return true
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val fragment = getCurrentFragment()
        if (fragment is HomeFragment) {
            binding.bottomNavigation.setSelectedItemId(R.id.bottomnav_home)
        }else  if (fragment is ContactFragment) {
            binding.bottomNavigation.setSelectedItemId(R.id.contact)
        }

    }

    fun AccessBottomNavigation(): BottomNavigationView {
        return binding.bottomNavigation
    }
    fun Toolbar()=binding.toolbar


}