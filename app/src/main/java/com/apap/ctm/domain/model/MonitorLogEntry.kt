package com.apap.ctm.domain.model

data class MonitorLogEntry(
    val name: String,
    val number: String,
    val duration: Int = 0,
    val beginning: String,
    val timesQueried: Int
) {
    override fun toString(): String = "::$name::$duration s"
}
