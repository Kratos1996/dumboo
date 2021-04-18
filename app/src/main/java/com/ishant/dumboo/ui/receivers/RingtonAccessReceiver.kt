package com.ishant.dumboo.ui.receivers

import android.R
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.ishant.dumboo.ui.home.HomeActivity

class RingtonAccessReceiver : BroadcastReceiver() {
    private var myManager: NotificationManager? = null
    private var audioManager: AudioManager? = null
    private var context: Context? = null
    override fun onReceive(context: Context, intent: Intent) {
        this.context = context
        myManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mynoti = NotificationCompat.Builder(context)
        try {
            if (Build.VERSION.SDK_INT < 23) {
                audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                if (ringTypeAccess != null) {
                    ringTypeAccess!!.RingType(audioManager!!.ringerMode)
                }
            } else if (Build.VERSION.SDK_INT >= 23) {
                requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp()
            }
        } catch (e: SecurityException) {
        }

        // For Normal mode
        mynoti.setContentTitle("SilentKiller")
        mynoti.setContentText("Ring Type Changed")
        mynoti.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        mynoti.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
        mynoti.setSmallIcon(R.drawable.ic_btn_speak_now)
        val il = Intent(context, HomeActivity::class.java)
        val pd = PendingIntent.getActivity(context, 0, il, 0)
        mynoti.setContentIntent(pd)
        mynoti.setAutoCancel(true)
        myManager!!.notify(1, mynoti.build())
    }

    interface GetPhoneRingTypeAccess {
        fun RingType(ringerMode: Int)
    }

    private fun requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp() {

        // if user granted access else ask for permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context != null && ringTypeAccess != null && myManager!!.isNotificationPolicyAccessGranted) {
                audioManager = context!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                ringTypeAccess!!.RingType(audioManager!!.ringerMode)
            } else {
                val intent = Intent(
                    Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
                )
                context!!.startActivity(intent)
            }
        }
    }

    companion object {
        var ringTypeAccess: GetPhoneRingTypeAccess? = null
    }
}