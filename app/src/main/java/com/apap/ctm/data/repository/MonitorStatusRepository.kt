package com.apap.ctm.data.repository

import com.apap.ctm.domain.model.MonitorStatus
import kotlinx.coroutines.flow.Flow

interface MonitorStatusRepository {
    suspend fun insertStatus(status: MonitorStatus)
    suspend fun deleteStatus(status: MonitorStatus)
    fun getStatus(): Flow<MonitorStatus>
}