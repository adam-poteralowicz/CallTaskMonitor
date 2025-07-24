package com.apap.ctm.data.repository

import com.apap.ctm.data.db.MonitorLogDao
import com.apap.ctm.data.db.MonitorLogEntryDao
import com.apap.ctm.data.mapper.MonitorLogEntryMapper
import com.apap.ctm.data.mapper.MonitorLogMapper
import com.apap.ctm.domain.model.MonitorLog
import com.apap.ctm.domain.model.MonitorLogEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MonitorLogRepositoryImpl @Inject constructor(
    private val dao: MonitorLogDao,
    private val entryDao: MonitorLogEntryDao,
    private val mapper: MonitorLogMapper,
    private  val entryMapper: MonitorLogEntryMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MonitorLogRepository {

    override suspend fun insertLog(log: MonitorLog) = withContext(dispatcher) {
        dao.insert(mapper.toEntity(log))
    }

    override suspend fun deleteLog() = withContext(dispatcher) {
        dao.delete()
    }

    override suspend fun getLog() : MonitorLog = withContext(dispatcher) {
        val entries = entryDao.getAll()
        dao.getLog()?.let {
            mapper.toDomain(it.copy(entries = entries))
        } ?: MonitorLog(entries = entries.map { entryMapper.toDomain(it) })
    }

    override fun getLogFlow() : Flow<MonitorLog> {
        val flow = dao.getLogFlow()
        return flow.map {
            mapper.toDomain(it)
        }
    }

    override suspend fun insertLogEntry(logEntry: MonitorLogEntry) {
        val entriesCount = entryDao.getAll().count()
        entryDao.insert(entryMapper.toEntity(entriesCount + 1, logEntry))
    }

    override suspend fun getAllEntries(): List<MonitorLogEntry> = entryDao.getAll().map { entryMapper.toDomain(it) }

    override suspend fun deleteEntries() = entryDao.delete()
}