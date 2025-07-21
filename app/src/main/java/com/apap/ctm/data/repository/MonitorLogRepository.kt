package com.apap.ctm.data.repository

import com.apap.ctm.domain.model.MonitorLog
import kotlinx.coroutines.flow.Flow

interface MonitorLogRepository {
    suspend fun insertLog(log: MonitorLog)
    suspend fun deleteLog(log: MonitorLog)
    fun getAllLogs() : Flow<List<MonitorLog>>
}