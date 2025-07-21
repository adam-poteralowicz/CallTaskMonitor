package com.apap.ctm.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.apap.ctm.R
import com.apap.ctm.presentation.viewmodel.MainViewModel
import com.apap.ctm.resources.SERVER_DETAILS_TOP_PADDING
import com.apap.ctm.resources.SPACER
import com.apap.ctm.util.MockedCallLog

private const val PORT = 8080

@Composable
fun MainScreen(
    ip: String,
    viewModel: MainViewModel = hiltViewModel()
) = Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    val isServerOnline = viewModel.serverStartedFlow.collectAsState()
    val shouldShowDialog = viewModel.showDialogFlow.collectAsState()

    if (shouldShowDialog.value.isNotEmpty()) {
        val permission = shouldShowDialog.value.joinToString()
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

    if (isServerOnline.value) {
        ServerDetails(
            address = ip,
            port = PORT
        )
        Spacer(Modifier.height(SPACER))
    } else {
        Spacer(Modifier.height(SERVER_DETAILS_TOP_PADDING))
    }
    ServerButtons(
        isServerOnline = isServerOnline.value,
        onServerOn = { viewModel.onServerToggled(true) },
        onServerOff = { viewModel.onServerToggled(false) }
    )
    Spacer(Modifier.height(SPACER))
    ServerLog()
}

@Composable
fun ServerDetails(address: String, port: Int) {
    Text(
        text = "HTTP Server at $address:$port",
        fontSize = 22.sp,
        modifier = Modifier.padding(top = SERVER_DETAILS_TOP_PADDING)
    )
}

@Composable
fun ServerButtons(
    isServerOnline: Boolean,
    onServerOn: () -> Unit,
    onServerOff: () -> Unit
) = Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceEvenly
) {
    Button(
        onClick = { onServerOn() },
        enabled = isServerOnline.not()
    ) {
        Text(stringResource(R.string.server_on))
    }
    Button(
        onClick = { onServerOff() },
        enabled = isServerOnline
    ) {
        Text(stringResource(R.string.server_off))
    }
}

@Composable
fun ServerLog() = LazyColumn {
    repeat(50) {
        item {
            MockedCallLog()
            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable
fun CallLogEntry(name: String, minutes: String, seconds: String) {
    Text(text = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(name)
        }
        append(stringResource(R.string.call_log_duration, minutes, seconds))
    })
}
