package com.apap.ctm.domain.model

data class MonitorRoot(
    val start: String,
    val services: List<MonitorService>
)
