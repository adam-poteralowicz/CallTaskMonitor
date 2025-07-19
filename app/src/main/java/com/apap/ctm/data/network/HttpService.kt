package com.apap.ctm.data.network

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import io.ktor.serialization.gson.gson
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.netty.util.internal.logging.InternalLoggerFactory
import io.netty.util.internal.logging.JdkLoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HttpService @Inject constructor() : Service() {

    private var server: NettyApplicationEngine? = null

    private companion object {
        const val PORT = 8080
    }

    override fun onCreate() {
        super.onCreate()
        Thread {
            InternalLoggerFactory.setDefaultFactory(JdkLoggerFactory.INSTANCE)
            server = embeddedServer(factory = Netty, port = PORT) {
                install(ContentNegotiation) { gson {} }
                callTaskController()
            }
            server?.start(wait = true)
        }.start()
    }

    override fun onBind(p0: Intent?): IBinder? = HttpServiceBinder()

    override fun onUnbind(intent: Intent?): Boolean {
        server?.stop(0,0 )
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

    private class HttpServiceBinder : Binder() {
        fun getService() = HttpService()
    }
}