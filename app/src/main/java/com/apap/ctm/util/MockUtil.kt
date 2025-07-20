package com.apap.ctm.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import com.apap.ctm.presentation.view.CallLogEntry
import java.security.SecureRandom

@Composable
fun MockedCallLog() = Row(horizontalArrangement = Arrangement.Center) {
    val name = (1..8).map { ('A'..'Z').random() }.joinToString("")
    val minutes = SecureRandom().nextInt().mod(60).toString()
    val seconds = (11..59).random().toString()

    CallLogEntry(name, minutes, seconds)
}