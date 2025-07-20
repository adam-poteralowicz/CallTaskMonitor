package com.apap.ctm.data.network

interface CallStatusCallback {
    fun onCallStarted(number: String)
    fun onCallEnded(number: String)
}