package com.apap.ctm.domain.model

data class MonitorLogEntry(
    val name: String,
    val number: String,
    val duration: Int,
    val beginning: String,
    val timesQueried: Int
)
