package com.apap.ctm.data.repository

import com.apap.ctm.domain.model.MonitorStatusEntity

interface MonitorStatusRepository {
    suspend fun insertStatus(status: MonitorStatusEntity)

    suspend fun updateStatus(number: String, ongoing: Boolean)
    suspend fun deleteStatus()
    suspend fun getStatus(): MonitorStatusEntity?
}