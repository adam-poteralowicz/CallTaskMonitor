package com.apap.ctm.util

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import com.apap.ctm.presentation.view.CallLogRow
import java.security.SecureRandom

@Composable
fun MockedCallLog() = Row(horizontalArrangement = Arrangement.Center) {
    val name = (1..8).map { ('A'..'Z').random() }.joinToString("")
    val seconds = SecureRandom().nextLong().mod(3600).toLong()
    val time = DateUtils.formatElapsedTime(seconds)

    CallLogRow(name, time)
}