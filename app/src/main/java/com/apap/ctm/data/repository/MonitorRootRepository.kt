package com.apap.ctm.data.repository

import com.apap.ctm.domain.model.MonitorRoot

interface MonitorRootRepository {
    suspend fun insertRoot(root: MonitorRoot)
    suspend fun deleteRoot()
    suspend fun getRoot(): MonitorRoot?
}