package com.apap.ctm.data.repository

import com.apap.ctm.data.db.MonitorStatusDao
import com.apap.ctm.data.mapper.MonitorStatusMapper
import com.apap.ctm.domain.model.MonitorStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MonitorStatusRepositoryImpl @Inject constructor(
    private val dao: MonitorStatusDao,
    private val mapper: MonitorStatusMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MonitorStatusRepository {

    override suspend fun insertStatus(status: MonitorStatus) = withContext(dispatcher) {
        dao.insert(mapper.toEntity(status))
    }

    override suspend fun updateStatus(number: String, ongoing: Boolean) = withContext(dispatcher) {
        dao.update(number, ongoing)
    }

    override suspend fun deleteStatus() = withContext(dispatcher) {
        dao.delete()
    }

    override suspend fun getStatus(): MonitorStatus? = withContext(dispatcher) {
        dao.getStatus()?.let { mapper.toDomain(it) }
    }
}