package com.ishant.dumboo.ui.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import java.util.*

class PhonecallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
        if (intent.action == "android.intent.action.NEW_OUTGOING_CALL") {
            savedNumber = intent.extras!!.getString("android.intent.extra.PHONE_NUMBER")
        } else {
            val stateStr = intent.extras!!.getString(TelephonyManager.EXTRA_STATE)
            val number = intent.extras!!.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
            var state = 0
            if (stateStr != null && stateStr == TelephonyManager.EXTRA_STATE_IDLE) {
                state = TelephonyManager.CALL_STATE_IDLE
            } else if (stateStr != null && stateStr == TelephonyManager.EXTRA_STATE_OFFHOOK) {
                state = TelephonyManager.CALL_STATE_OFFHOOK
            } else if (stateStr != null && stateStr == TelephonyManager.EXTRA_STATE_RINGING) {
                state = TelephonyManager.CALL_STATE_RINGING
            }
            if (stateStr != null) onCallStateChanged(context, state, number)
        }
    }

    interface CallAccess {
        fun onIncomingCallReceived(ctx: Context?, number: String?, start: Date?)
        fun onIncomingCallAnswered(ctx: Context?, number: String?, start: Date?)
        fun onIncomingCallEnded(ctx: Context?, number: String?, start: Date?, end: Date?)
        fun onOutgoingCallStarted(ctx: Context?, number: String?, start: Date?)
        fun onOutgoingCallEnded(ctx: Context?, number: String?, start: Date?, end: Date?)
        fun onMissedCall(context: Context?, savedNumber: String?, callStartTime: Date?)
    }

    //Deals with actual events
    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    fun onCallStateChanged(context: Context?, state: Int, number: String?) {
        if (lastState == state) {
            //No change, debounce extras
            return
        }
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                isIncoming = true
                callStartTime = Date()
                savedNumber = number
                if (callAccess != null) callAccess!!.onIncomingCallReceived(
                    context,
                    number,
                    callStartTime
                )
            }
            TelephonyManager.CALL_STATE_OFFHOOK ->                 //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false
                    callStartTime = Date()
                    if (callAccess != null) callAccess!!.onOutgoingCallStarted(
                        context,
                        savedNumber,
                        callStartTime
                    )
                } else {
                    isIncoming = true
                    callStartTime = Date()
                    if (callAccess != null) callAccess!!.onIncomingCallAnswered(
                        context,
                        savedNumber,
                        callStartTime
                    )
                }
            TelephonyManager.CALL_STATE_IDLE ->                 //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss
                    if (callAccess != null) callAccess!!.onMissedCall(
                        context,
                        savedNumber,
                        callStartTime
                    )
                } else if (isIncoming) {
                    if (callAccess != null) callAccess!!.onIncomingCallEnded(
                        context,
                        savedNumber,
                        callStartTime,
                        Date()
                    )
                } else {
                    if (callAccess != null) callAccess!!.onOutgoingCallEnded(
                        context,
                        savedNumber,
                        callStartTime,
                        Date()
                    )
                }
        }
        lastState = state
    }

    companion object {
        //The receiver will be recreated whenever android feels like it.  We need a static variable to remember data between instantiations
        private var lastState = TelephonyManager.CALL_STATE_IDLE
        private var callStartTime: Date? = null
        private var isIncoming = false
        private var savedNumber //because the passed incoming is only valid in ringing
                : String? = null
        var callAccess: CallAccess? = null
    }
}