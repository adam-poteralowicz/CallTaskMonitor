package com.apap.ctm.data.repository

import com.apap.ctm.data.db.MonitorRootDao
import com.apap.ctm.domain.model.MonitorRoot
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MonitorRootRepository @Inject constructor(
    private val dao: MonitorRootDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun insertRoot(root: MonitorRoot) = withContext(dispatcher) {
        dao.insert(root)
    }

    suspend fun deleteRoot(root: MonitorRoot) = withContext(dispatcher) {
        dao.delete(root)
    }

    fun getRoot(): Flow<MonitorRoot> = dao.getRoot()
}