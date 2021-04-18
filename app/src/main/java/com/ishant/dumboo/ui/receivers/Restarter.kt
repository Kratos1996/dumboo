package com.ishant.dumboo.ui.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.ishant.dumboo.service.MyService

class Restarter : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.i("BroadcastListened", "Service tried to stop")
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, MyService::class.java))
        } else {
            context.startService(Intent(context, MyService::class.java))
        }
    }
}