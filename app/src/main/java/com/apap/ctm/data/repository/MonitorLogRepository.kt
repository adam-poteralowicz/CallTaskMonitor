package com.apap.ctm.data.repository

import com.apap.ctm.domain.model.MonitorLog
import com.apap.ctm.domain.model.MonitorLogEntry

interface MonitorLogRepository {
    suspend fun insertLog(log: MonitorLog)
    suspend fun deleteLog()
    suspend fun getLog() : MonitorLog
    suspend fun insertLogEntry(logEntry: MonitorLogEntry)
    suspend fun getAllEntries() : List<MonitorLogEntry>

    suspend fun deleteEntries()
}