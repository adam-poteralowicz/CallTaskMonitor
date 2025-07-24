package com.apap.ctm.data.repository

import com.apap.ctm.data.db.MonitorStatusDao
import com.apap.ctm.data.model.MonitorStatusEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MonitorStatusRepositoryImpl @Inject constructor(
    private val dao: MonitorStatusDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MonitorStatusRepository {

    override suspend fun insertStatus(status: MonitorStatusEntity) = withContext(dispatcher) {
        dao.insert(status)
    }

    override suspend fun updateStatus(number: String, ongoing: Boolean) = withContext(dispatcher) {
        dao.update(number, ongoing)
    }

    override suspend fun deleteStatus() = withContext(dispatcher) {
        dao.delete()
    }

    override suspend fun getStatus(): MonitorStatusEntity? = withContext(dispatcher) {
        dao.getStatus()
    }
}