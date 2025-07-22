package com.apap.ctm.data.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager

class CallStatusBroadcastReceiver(
    private val callback: CallStatusCallback
) : BroadcastReceiver() {

    private companion object {
        const val IDLE = "IDLE"
        const val OFF_HOOK = "OFFHOOK"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val number = intent.extras?.getString(TelephonyManager.EXTRA_INCOMING_NUMBER) ?: return
            val status = intent.extras?.getString(TelephonyManager.EXTRA_STATE) ?: return
            broadcastCallStatus(status, number)
        }
    }

    private fun broadcastCallStatus(status: String, number: String) {
        when (status) {
            OFF_HOOK -> callback.onCallStarted(number)
            IDLE -> callback.onCallEnded(number)
        }
    }
}