package com.ishant.dumboo.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioManager
import android.os.Build
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ishant.dumboo.R
import com.ishant.dumboo.application.DumbooApplication
import com.ishant.dumboo.database.prefrence.SharedPre
import com.ishant.dumboo.database.roomdatabase.ContactList
import com.ishant.dumboo.database.roomdatabase.DatabaseRepository
import com.ishant.dumboo.ui.receivers.PhonecallReceiver
import com.ishant.dumboo.ui.receivers.Restarter
import com.ishant.dumboo.ui.receivers.RingtonAccessReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MyService : Service(), PhonecallReceiver.CallAccess,
    RingtonAccessReceiver.GetPhoneRingTypeAccess,
    TextToSpeech.OnInitListener {
    private val NOTIF_ID = 1
    private val NOTIF_CHANNEL_ID = "Channel_Id"
    var counter = 0
    private var ringAccessListner: RingtonAccessReceiver.GetPhoneRingTypeAccess? = null
    private var callAccess: PhonecallReceiver.CallAccess? = null
    var isSilentModeActivated = false
    var geAllFavContacts = ArrayList<ContactList>()
    var isSupportedTextToSppech = false

    @Inject
    lateinit var am: AudioManager

    @Inject
    lateinit var repo: DatabaseRepository

    @Inject
    lateinit var sharedPre: SharedPre

    lateinit var tts: TextToSpeech

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(this, this)
        ringAccessListner = this
        callAccess = this
        (applicationContext as DumbooApplication).setRingListner(ringAccessListner)
        (applicationContext as DumbooApplication).setCallListener(callAccess)
        when (am.getRingerMode()) {
            AudioManager.RINGER_MODE_SILENT -> isSilentModeActivated = true
            AudioManager.RINGER_MODE_VIBRATE -> isSilentModeActivated = true
            AudioManager.RINGER_MODE_NORMAL -> isSilentModeActivated = false
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForeground()
        } else {
            startForeground(1, Notification())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startForeground() {
        val NOTIFICATION_CHANNEL_ID = NOTIF_CHANNEL_ID
        val name: CharSequence = getString(R.string.app_name)
        val description = getString(R.string.app_name)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            name,
            importance
        )
        val logo = BitmapFactory.decodeResource(resources, R.drawable.logo)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val content = "Dumboo Service Running in Background"
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(content)
            .setLargeIcon(logo)
            .setColorized(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan.description = description
            chan.enableVibration(true)
            chan.setSound(null, null)
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            manager.createNotificationChannel(chan)
            manager.notify(NOTIF_ID, notificationBuilder.build())
        }
        startForeground(NOTIF_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (tts != null) {
            tts.stop()
            tts.shutdown()
        }
        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, Restarter::class.java)
        this.sendBroadcast(broadcastIntent)
    }


    override fun onIncomingCallReceived(ctx: Context?, number: String?, start: Date?) {
        GlobalScope.launch {
            var geAllFavContacts = async { repo.GetContact(number!!, true) }
            if (geAllFavContacts.await() != null) {
                if (isSilentModeActivated) {
                    am.ringerMode = AudioManager.RINGER_MODE_NORMAL
                }
                if (sharedPre.getNameAnnouncer() && isSupportedTextToSppech) {
                    tts.setSpeechRate(0.5f)
                    tts.speak(
                        "Hello Boss Call From " + geAllFavContacts.await().Name,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        ""
                    )
                }
            } else {
                if (sharedPre.getNameAnnouncer() && isSupportedTextToSppech) {
                    tts.speak(
                        "Hello Boss Call From Unknown Number " + number,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        ""
                    )
                }
            }
        }
    }


    override fun onIncomingCallAnswered(ctx: Context?, number: String?, start: Date?) {
        Toast.makeText(ctx, "Call Answered", Toast.LENGTH_SHORT).show()
        if (isSilentModeActivated) {
            am.ringerMode = AudioManager.RINGER_MODE_SILENT
        }
    }

    override fun onIncomingCallEnded(ctx: Context?, number: String?, start: Date?, end: Date?) {
        Toast.makeText(ctx, "Call Ended", Toast.LENGTH_SHORT).show()
        if (isSilentModeActivated) {
            am.ringerMode = AudioManager.RINGER_MODE_SILENT
        }
    }

    override fun onOutgoingCallStarted(ctx: Context?, number: String?, start: Date?) {
        Toast.makeText(ctx, "Call Started", Toast.LENGTH_SHORT).show()
        if (isSilentModeActivated) {
            am.ringerMode = AudioManager.RINGER_MODE_SILENT
        }
    }

    override fun onOutgoingCallEnded(ctx: Context?, number: String?, start: Date?, end: Date?) {
        Toast.makeText(ctx, "Call Cancel", Toast.LENGTH_SHORT).show()
        if (isSilentModeActivated) {
            am.ringerMode = AudioManager.RINGER_MODE_SILENT
        }
    }

    override fun onMissedCall(context: Context?, savedNumber: String?, callStartTime: Date?) {
        Toast.makeText(context, "Missed Call Access ", Toast.LENGTH_SHORT).show()
        if (isSilentModeActivated) {
            am.ringerMode = AudioManager.RINGER_MODE_SILENT
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

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts.setLanguage(Locale.US)
            isSupportedTextToSppech =
                !(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)

        } else {

        }
    }

}