package com.apap.ctm.presentation.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.provider.CallLog
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.apap.ctm.data.network.HttpService
import com.apap.ctm.presentation.viewmodel.MainViewModel
import com.apap.ctm.ui.theme.CallTaskMonitorTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val serverIntent by lazy { Intent(this, HttpService::class.java) }
    val serviceConnection by lazy { HttpService.HttpServiceConnection() }

    private companion object {
        const val PERMISSIONS_REQUEST_CODE = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CallTaskMonitorTheme {
                MainScreen()
            }
        }
        setUpObservers()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        if ((requestCode == PERMISSIONS_REQUEST_CODE).and(grantResults.all { it == PERMISSION_GRANTED })) {
            startServer()
        }
        else {
            val notGranted = mutableListOf<String>()
            grantResults.filterNot { it == PERMISSION_GRANTED }.map { grantResults.indexOf(it) }.onEach { index ->
                permissions[index]?.let { notGranted += it }
            }
            viewModel.onPermissionsNotGranted(notGranted)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
    }

    private fun setUpObservers() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.toggleServerFlow.collectLatest {
                    onServerToggled(it)
                }
            }
        }
    }

    private fun onServerToggled(shouldTurnOn: Boolean) {
        if (shouldTurnOn) {
            ensurePermissionsGranted()
        } else {
            unbindService(serviceConnection)
            viewModel.onServerStopped()
        }
    }

    private fun startServer() {
        bindService(serverIntent, serviceConnection, BIND_AUTO_CREATE)
        viewModel.onServerStarted()
        fetchCallLog()
    }

    private fun fetchCallLog() {
        val cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC")
        viewModel.onCallLogFetched(cursor)
    }

    private fun ensurePermissionsGranted() {
        var permissions = arrayOf<String>()
        arrayOf(
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE
        ).onEach {
            if (ContextCompat.checkSelfPermission(this, it) != PERMISSION_GRANTED) {
                permissions += it
            }
        }
        return if (permissions.isEmpty()) {
            startServer()
        } else {
            ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    CallTaskMonitorTheme {
        MainScreen()
    }
}