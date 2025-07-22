package com.apap.ctm.data.repository

import com.apap.ctm.domain.model.MonitorLog
import kotlinx.coroutines.flow.Flow

interface MonitorLogRepository {
    suspend fun insertLog(log: MonitorLog)
    suspend fun deleteLog()
    suspend fun getLog() : MonitorLog?
    fun getLogFlow() : Flow<MonitorLog?>
}