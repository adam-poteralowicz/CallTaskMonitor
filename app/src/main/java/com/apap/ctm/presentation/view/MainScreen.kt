package com.apap.ctm.presentation.view

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.apap.ctm.R
import com.apap.ctm.data.model.MonitorLogEntryEntity
import com.apap.ctm.presentation.viewmodel.MainViewModel
import com.apap.ctm.resources.DIVIDER
import com.apap.ctm.resources.SERVER_DETAILS_TOP_PADDING
import com.apap.ctm.resources.SPACER

private const val PORT = 8080

@Composable
fun MainScreen(
    ip: String,
    viewModel: MainViewModel = hiltViewModel()
) = Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    val state = viewModel.state.collectAsState()
    val log = viewModel.log.collectAsState()

    if (state.value.showPermissionDialog.isNotEmpty()) {
        val permission = state.value.showPermissionDialog.joinToString()
        AlertDialog(
            onDismissRequest = { viewModel.onPermissionsNotGranted(emptyList()) },
            title = { Text(stringResource(R.string.alert_dialog_title)) },
            text = {
                Text(
                    text = stringResource(
                        R.string.alert_dialog_msg,
                        stringResource(R.string.permission_name, permission)
                    )
                )
            },
            confirmButton = {
                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    viewModel.onPermissionsNotGranted(emptyList())
                }) {
                    Text(text = stringResource(R.string.alert_dialog_positive_button))
                }
            }
        )
    }

    if (state.value.isServerStarted) {
        ServerDetails(
            address = ip,
            port = PORT
        )
        Spacer(Modifier.height(SPACER))
    } else {
        Spacer(Modifier.height(SERVER_DETAILS_TOP_PADDING))
    }
    ServerButton(
        isServerOnline = state.value.isServerStarted,
        onServerOn = { viewModel.onServerToggled(true) },
        onServerOff = { viewModel.onServerToggled(false) }
    )
    Spacer(Modifier.height(SPACER))
    if (state.value.isServerStarted) {
        val entries = log.value?.entries
        when {
            entries?.isNotEmpty() == true -> ServerLog(entries)
            else -> ServerLogEmptyState()
        }
    }
}

@Composable
fun ServerDetails(address: String, port: Int) {
    Text(
        text = stringResource(R.string.server_details, address, port),
        fontSize = 22.sp,
        modifier = Modifier.padding(top = SERVER_DETAILS_TOP_PADDING)
    )
}

@Composable
fun ServerButton(
    isServerOnline: Boolean,
    onServerOn: () -> Unit,
    onServerOff: () -> Unit
) = Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center
) {
    Button(
        onClick = { if (isServerOnline) onServerOff() else onServerOn() },
        colors = ButtonDefaults.buttonColors(containerColor = if (isServerOnline) Color.Red else Color.Green)
    ) {
        Text(stringResource(if (isServerOnline) R.string.server_off else R.string.server_on))
    }
}

@Composable
fun ServerLog(entries: List<MonitorLogEntryEntity>) {
    LazyColumn(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        items(entries) { entry ->
            HorizontalDivider(thickness = DIVIDER)
            val time = DateUtils.formatElapsedTime(entry.duration.toLong())
            entry.name?.let { name ->
                CallLogRow(name, time)
            }
        }
    }
}

@Composable
fun ServerLogEmptyState() = Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) { Text(stringResource(R.string.server_log_empty_state_msg)) }

@Composable
fun CallLogRow(name: String, time: String) {
    Text(text = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(name)
        }
        append(": $time")
    })
}