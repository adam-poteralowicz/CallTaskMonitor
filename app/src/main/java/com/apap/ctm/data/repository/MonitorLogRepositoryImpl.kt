package com.apap.ctm.data.repository

import com.apap.ctm.data.db.MonitorLogDao
import com.apap.ctm.domain.model.MonitorLog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MonitorLogRepositoryImpl @Inject constructor(
    private val dao: MonitorLogDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MonitorLogRepository {

    override suspend fun insertLog(log: MonitorLog) = withContext(dispatcher) {
        dao.insert(log)
    }

    override suspend fun deleteLog(log: MonitorLog) = withContext(dispatcher) {
        dao.delete(log)
    }

    override fun getAllLogs() : Flow<List<MonitorLog>> = dao.getAll()
}