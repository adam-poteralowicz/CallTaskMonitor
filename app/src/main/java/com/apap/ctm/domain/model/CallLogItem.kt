package com.apap.ctm.domain.model

import java.util.Date

data class CallLogItem(
    val name: String,
    val date: Date,
    val duration: String,
    val number: String,
    val type: CallType
)

enum class CallType(value: String) {
    INCOMING("Incoming"),
    MISSED("Missed"),
    OUTGOING("Outgoing")
}