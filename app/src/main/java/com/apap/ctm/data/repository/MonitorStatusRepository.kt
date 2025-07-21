package com.apap.ctm.data.repository

import com.apap.ctm.data.db.MonitorStatusDao
import com.apap.ctm.domain.model.MonitorStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MonitorStatusRepository @Inject constructor(
    private val dao: MonitorStatusDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun insertStatus(status: MonitorStatus) = withContext(dispatcher) {
        dao.insert(status)
    }

    suspend fun deleteStatus(status: MonitorStatus) = withContext(dispatcher) {
        dao.delete(status)
    }

    fun getStatus(): Flow<MonitorStatus> = dao.getStatus()
}