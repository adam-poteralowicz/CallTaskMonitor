package com.apap.ctm.data.network

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import com.apap.ctm.R
import com.apap.ctm.util.getLocalIPAddress
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.netty.util.internal.logging.InternalLoggerFactory
import io.netty.util.internal.logging.JdkLoggerFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@AndroidEntryPoint
class HttpService : Service() {

    @Inject lateinit var callTaskController: CallTaskController

    private var server: NettyApplicationEngine? = null
    private val cursor by lazy { contentResolver.query(ContactsContract.Data.CONTENT_URI, null, null, null, null) }

    private val callStatusBroadcastReceiver by lazy {
        val callback = object : CallStatusCallback {
            override fun onCallStarted(number: String) {
                coroutineScope.launch {
                    cursor?.let {
                        callTaskController.startCall(it, number)
                    }
                }
            }

            override fun onCallEnded(number: String) {
                coroutineScope.launch {
                    callTaskController.stopCall()
                    cursor?.let {
                        callTaskController.addLogEntry(it)
                    }
                }
            }
        }
        CallStatusBroadcastReceiver(callback)
    }

    private val localIP by lazy { getLocalIPAddress(applicationContext) }

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private companion object {
        const val DEFAULT_NETWORK_INTERFACE = "0.0.0.0"
        const val PORT = 8080
    }

    override fun onCreate() {
        super.onCreate()
        coroutineScope.launch {
            registerCallStatusBroadcastReceiver()
            InternalLoggerFactory.setDefaultFactory(JdkLoggerFactory.INSTANCE)
            server = embeddedServer(
                factory = Netty,
                port = PORT,
                host = localIP.ifBlank { DEFAULT_NETWORK_INTERFACE }
            ) {
                setUp(callTaskController)
            }
            server?.start(wait = false)
        }.start()
    }

    override fun onDestroy() {
        cursor?.close()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = HttpServiceBinder()

    override fun onUnbind(intent: Intent?): Boolean {
        unregisterReceiver(callStatusBroadcastReceiver)
        server?.stop(0,0 )
        coroutineScope.launch {
            callTaskController.clearAllTables()
        }
        return false
    }

    class HttpServiceConnection : ServiceConnection {
        private var httpService: HttpService? = null

        override fun onServiceConnected(className: ComponentName?, binder: IBinder) {
            httpService = (binder as HttpServiceBinder).getService()
        }

        override fun onServiceDisconnected(className: ComponentName?) {
            httpService = null
        }
    }

    private fun registerCallStatusBroadcastReceiver() {
        val callFilter = IntentFilter()
        callFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        registerReceiver(callStatusBroadcastReceiver, callFilter)
        coroutineScope.launch {
            val log = getString(R.string.log)
            val status = getString(R.string.status)
            callTaskController.addService(name = status, uri = "$localIP:$PORT/$status")
            callTaskController.addService(name = log, uri = "$localIP:$PORT/$log")
        }
    }

    private class HttpServiceBinder : Binder() {
        fun getService() = HttpService()
    }
}