package com.ishant.dumboo.application
/*Android Developer
* Ishant Sharma
* Java and Kotlin
*
* */
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Build
import androidx.multidex.MultiDexApplication
import com.ishant.dumboo.service.MyService
import com.ishant.dumboo.ui.receivers.PhonecallReceiver
import com.ishant.dumboo.ui.receivers.RingtonAccessReceiver
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp(MultiDexApplication::class)
class DumbooApplication  : Hilt_DumbooApplication(){
    private var connectionReceiver: PhonecallReceiver? = null
    private var ringtonAccessReceiver: RingtonAccessReceiver? = null
    private val networkintentFilter = IntentFilter()
    var filter = IntentFilter(
        AudioManager.RINGER_MODE_CHANGED_ACTION
    )
    override fun onCreate() {
        super.onCreate()
        connectionReceiver = PhonecallReceiver()
        ringtonAccessReceiver = RingtonAccessReceiver()
        registerReceiver(connectionReceiver, networkintentFilter)
        registerReceiver(connectionReceiver, filter)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, MyService::class.java))
        } else {
            startService(Intent(this, MyService::class.java))
        }
    }
    fun setConnectionListener(listener: PhonecallReceiver.CallAccess?) {
        PhonecallReceiver.callAccess = listener
    }

    fun setRingListner(listener: RingtonAccessReceiver.GetPhoneRingTypeAccess?) {
        RingtonAccessReceiver.ringTypeAccess = listener
    }
   /* override fun onTerminate() {
        super.onTerminate()
        unregisterReceiver(connectionReceiver)
        unregisterReceiver(ringtonAccessReceiver)
    }*/
}