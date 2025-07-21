package com.apap.ctm.data.repository

import com.apap.ctm.domain.model.MonitorRoot
import kotlinx.coroutines.flow.Flow

interface MonitorRootRepository {
    suspend fun insertRoot(root: MonitorRoot)
    suspend fun deleteRoot(root: MonitorRoot)
    fun getRoot(): Flow<MonitorRoot>
}