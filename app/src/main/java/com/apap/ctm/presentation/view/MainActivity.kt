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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        askForReadCallPermissionIfNeeded()
        setContent {
            CallTaskMonitorTheme {
                MainScreen()
            }
        }
        setUpObservers()
    }

    private fun setUpObservers() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.toggleServerFlow.collectLatest {
                    startServer(it)
                }
            }
        }
    }

    private fun startServer(shouldTurnOn: Boolean) {
        if (shouldTurnOn) {
            bindService(serverIntent, serviceConnection, BIND_AUTO_CREATE)
            fetchCallLog()
        } else {
            unbindService(serviceConnection)
        }
    }

    private fun askForReadCallPermissionIfNeeded() {
        val permission = Manifest.permission.READ_CALL_LOG
        if (ContextCompat.checkSelfPermission(this, permission) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 1)
        }
    }

    private fun fetchCallLog() {
        val cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC")
        viewModel.onCallLogFetched(cursor)
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    CallTaskMonitorTheme {
        MainScreen()
    }
}