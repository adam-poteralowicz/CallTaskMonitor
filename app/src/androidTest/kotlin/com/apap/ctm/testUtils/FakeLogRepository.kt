package com.apap.ctm.testUtils

import com.apap.ctm.data.repository.MonitorLogRepository
import com.apap.ctm.domain.model.MonitorLog
import com.apap.ctm.domain.model.MonitorLogEntry

class FakeLogRepository : MonitorLogRepository {

    private var fakeLog: MonitorLog? = null
    private var fakeLogEntries = listOf(randomLogEntry())

    override suspend fun insertLog(log: MonitorLog) {
        fakeLog = log
    }

    override suspend fun deleteLog() {
        fakeLog = null
    }

    override suspend fun getLog(): MonitorLog = fakeLog ?: MonitorLog().copy(entries = fakeLogEntries)

    override suspend fun insertLogEntry(logEntry: MonitorLogEntry) {
        fakeLogEntries = listOf(logEntry)
    }

    override suspend fun getAllEntries(): List<MonitorLogEntry> = fakeLogEntries

    override suspend fun deleteEntries() {
        fakeLogEntries = listOf()
    }
}