package com.apap.ctm.data.repository

import com.apap.ctm.data.db.MonitorLogDao
import com.apap.ctm.data.model.MonitorLogEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MonitorLogRepositoryImpl @Inject constructor(
    private val dao: MonitorLogDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MonitorLogRepository {

    override suspend fun insertLog(log: MonitorLogEntity) = withContext(dispatcher) {
        dao.insert(log)
    }

    override suspend fun deleteLog() = withContext(dispatcher) {
        dao.delete()
    }

    override suspend fun getLog() : MonitorLogEntity? = withContext(dispatcher) {
        dao.getLog()
    }

    override fun getLogFlow() : Flow<MonitorLogEntity?> = dao.getLogFlow()
}