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
import java.util.*

class HomeActivity : DumbooBaseActivity(), NavigationView.OnNavigationItemSelectedListener {
private lateinit var binding:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_home)
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected)
        binding.toolbar.switch1.isChecked = !sharedPre.isStopService()
        binding.toolbar.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPre.StopService(!isChecked)
        }
        startFragment(HomeFragment.newInstance(), HomeFragment.toString(), true)
    }

    override fun onIncomingCallReceived(ctx: Context?, number: String?, start: Date?) {

    }


    override fun onIncomingCallAnswered(ctx: Context?, number: String?, start: Date?) {
        Toast.makeText(ctx, "Call Answered", Toast.LENGTH_SHORT).show()
        if (isSilentModeActivated) {
            am!!.ringerMode = AudioManager.RINGER_MODE_SILENT
        }
    }

    override fun onIncomingCallEnded(ctx: Context?, number: String?, start: Date?, end: Date?) {
        Toast.makeText(ctx, "Call Ended", Toast.LENGTH_SHORT).show()
        if (isSilentModeActivated) {
            am!!.ringerMode = AudioManager.RINGER_MODE_SILENT
        }
    }

    override fun onOutgoingCallStarted(ctx: Context?, number: String?, start: Date?) {
        Toast.makeText(ctx, "Call Started", Toast.LENGTH_SHORT).show()
        if (isSilentModeActivated) {
            am!!.ringerMode = AudioManager.RINGER_MODE_SILENT
        }
    }

    override fun onOutgoingCallEnded(ctx: Context?, number: String?, start: Date?, end: Date?) {
        Toast.makeText(ctx, "Call Cancel", Toast.LENGTH_SHORT).show()
        if (isSilentModeActivated) {
            am!!.ringerMode = AudioManager.RINGER_MODE_SILENT
        }
    }

    override fun onMissedCall(context: Context?, savedNumber: String?, callStartTime: Date?) {
        Toast.makeText(context, "Missed Call Access ", Toast.LENGTH_SHORT).show()
        if (isSilentModeActivated) {
            am!!.ringerMode = AudioManager.RINGER_MODE_SILENT
        }
    }

    override fun RingType(ringerMode: Int) {
        when (ringerMode) {
            AudioManager.RINGER_MODE_SILENT -> isSilentModeActivated = true
            AudioManager.RINGER_MODE_VIBRATE -> isSilentModeActivated = true
            AudioManager.RINGER_MODE_NORMAL -> {
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bottomnav_home -> {
                if (getCurrentFragment() !is HomeFragment)
                    startFragment(HomeFragment.newInstance(), true, HomeFragment.toString())
            }
            R.id.contact -> {
                if (getCurrentFragment() !is ContactFragment)
                    startFragment(ContactFragment.newInstance(), true, ContactFragment.toString())
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


}