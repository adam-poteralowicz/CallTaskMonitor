package com.apap.ctm.data.repository

import com.apap.ctm.data.model.MonitorLogEntity
import kotlinx.coroutines.flow.Flow

interface MonitorLogRepository {
    suspend fun insertLog(log: MonitorLogEntity)
    suspend fun deleteLog()
    suspend fun getLog() : MonitorLogEntity?
    fun getLogFlow() : Flow<MonitorLogEntity?>
}