package com.apap.ctm.util

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import java.security.SecureRandom

private const val PHONE_NUMBER_LIMIT = 999999999

@Composable
fun MockedCallLog() {
    Text(
        "Call from ${(1..8).map { ('A'..'Z').random() }.joinToString("")} " +
                "to ${SecureRandom().nextInt().mod(PHONE_NUMBER_LIMIT)}"
    )
}