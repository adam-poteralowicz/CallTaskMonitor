package com.apap.ctm.domain.model

data class MonitorStatus(
    val number: String,
    val ongoing: Boolean,
    val duration: Int,
    val name: String,
    val start: String,
    val stop: String,
)
