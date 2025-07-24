package com.apap.ctm.data.repository

import com.apap.ctm.data.db.MonitorRootDao
import com.apap.ctm.data.mapper.MonitorRootMapper
import com.apap.ctm.domain.model.MonitorRoot
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MonitorRootRepositoryImpl @Inject constructor(
    private val dao: MonitorRootDao,
    private val mapper: MonitorRootMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MonitorRootRepository {

    override suspend fun insertRoot(root: MonitorRoot) = withContext(dispatcher) {
        dao.insert(mapper.toEntity(root))
    }

    override suspend fun deleteRoot() = withContext(dispatcher) {
        dao.delete()
    }

    override suspend fun getRoot(): MonitorRoot? = withContext(dispatcher) {
        dao.getRoot()?.let { mapper.toDomain(it) }
    }
}