package com.apap.ctm.data.repository

import com.apap.ctm.domain.model.MonitorStatus

interface MonitorStatusRepository {
    suspend fun insertStatus(status: MonitorStatus)

    suspend fun updateStatus(number: String, ongoing: Boolean)
    suspend fun deleteStatus()
    suspend fun getStatus(): MonitorStatus?
}