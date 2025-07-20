package com.apap.ctm.util

import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.text.format.Formatter

fun getLocalIPAddress(context: Context) : String {
    val wifiManager = context.applicationContext.getSystemService(WIFI_SERVICE)
    val connectionInfo = (wifiManager as WifiManager).connectionInfo
    return Formatter.formatIpAddress(connectionInfo.ipAddress)
}