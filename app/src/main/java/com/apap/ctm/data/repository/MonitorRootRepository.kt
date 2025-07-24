package com.apap.ctm.data.repository

import com.apap.ctm.data.model.MonitorRootEntity

interface MonitorRootRepository {
    suspend fun insertRoot(root: MonitorRootEntity)
    suspend fun deleteRoot()
    suspend fun getRoot(): MonitorRootEntity?
}