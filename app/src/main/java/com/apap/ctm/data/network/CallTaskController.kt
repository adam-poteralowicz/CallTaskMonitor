package com.apap.ctm.data.network

import android.database.Cursor
import com.apap.ctm.data.repository.MonitorLogRepository
import com.apap.ctm.data.repository.MonitorRootRepository
import com.apap.ctm.data.repository.MonitorStatusRepository
import com.apap.ctm.domain.model.MonitorLog
import com.apap.ctm.domain.model.MonitorLogEntry
import com.apap.ctm.domain.model.MonitorRoot
import com.apap.ctm.domain.model.MonitorService
import com.apap.ctm.domain.model.MonitorStatus
import com.apap.ctm.domain.usecase.GetNameFromContacts
import com.apap.ctm.util.toDateTime
import com.apap.ctm.util.toDateTimeString
import org.joda.time.DateTime
import javax.inject.Inject

class CallTaskController @Inject constructor(
    private val logRepository: MonitorLogRepository,
    private val rootRepository: MonitorRootRepository,
    private val statusRepository: MonitorStatusRepository,
    private val getNameFromContacts: GetNameFromContacts
) {

    suspend fun startCall(cursor: Cursor, number: String) {
        val now = DateTime.now().toDateTimeString()
        val status = MonitorStatus(
            start = now,
            stop = now,
            ongoing = true,
            number = number,
            name = getNameFromContacts.invoke(cursor)
        )
        statusRepository.insertStatus(status)
    }

    suspend fun stopCall() {
        with(statusRepository.getStatus()) {
            this ?: return@with
            val now = DateTime.now()
            val start = start.toDateTime().millis
            val stop = now.millis
            val duration = ((stop - start)/1000).toInt()
            val status = copy(
                ongoing = false,
                stop = now.toDateTimeString(),
                duration = duration
            )
            statusRepository.insertStatus(status)
        }
    }

    suspend fun addLogEntry(cursor: Cursor) {
        val status = statusRepository.getStatus() ?: return
        val log = logRepository.getLog()

        val logEntry = MonitorLogEntry(
            beginning = status.start,
            duration = status.duration,
            number = status.number,
            name = getNameFromContacts.invoke(cursor),
            timesQueried = 0 // number of times log entry queried via API - initially it's zero
        )
        logRepository.insertLogEntry(logEntry)
        val entries = logRepository.getAllEntries()
        val updatedEntries = if (entries.isEmpty()) {
            listOf(logEntry)
        } else {
            entries.toMutableList().plus(logEntry)
        }
        logRepository.insertLog(log.copy(entries = updatedEntries))
    }

    suspend fun updateLogEntry(logEntry: MonitorLogEntry) {
        logRepository.insertLogEntry(logEntry)
    }

    suspend fun addServices(services: List<MonitorService>) {
        val rootFromDb = rootRepository.getRoot()
        rootFromDb?.let {
            if (it.services.isEmpty()) {
                rootRepository.insertRoot(it.copy(services = services))
            }
        } ?: run {
            val root = MonitorRoot(
                start = DateTime.now().toDateTimeString(),
                services = services
            )
            rootRepository.insertRoot(root)
        }
    }

    suspend fun insertLog(log: MonitorLog) {
        logRepository.insertLog(log)
    }

    suspend fun getServices() = rootRepository.getRoot()

    suspend fun getStatus() = statusRepository.getStatus()

    suspend fun getLog() = logRepository.getLog()

    suspend fun clearAllTables() {
        rootRepository.deleteRoot()
        statusRepository.deleteStatus()
    }

    suspend fun clearEntries() {
        logRepository.deleteEntries()
    }
}