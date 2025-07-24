package com.apap.ctm.data.network

import android.database.Cursor
import android.util.Log
import com.apap.ctm.data.repository.MonitorLogRepository
import com.apap.ctm.data.repository.MonitorRootRepository
import com.apap.ctm.data.repository.MonitorStatusRepository
import com.apap.ctm.data.model.MonitorLogEntity
import com.apap.ctm.data.model.MonitorLogEntryEntity
import com.apap.ctm.data.model.MonitorRootEntity
import com.apap.ctm.data.model.MonitorServiceEntity
import com.apap.ctm.data.model.MonitorStatusEntity
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
        val status = MonitorStatusEntity(
            start = now,
            stop = now,
            ongoing = true,
            number = number,
            name = getNameFromContacts.invoke(cursor)
        )
        Log.d("CallTaskController::startCall", "name: ${status.name}, number: ${status.number}")
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
            Log.d("CallTaskController::stopCall", "name: $name, number: ${status.number}, duration: ${status.duration}")
            statusRepository.insertStatus(status)
        }
    }

    suspend fun addLogEntry(cursor: Cursor) {
        val status = statusRepository.getStatus() ?: return
        val log = logRepository.getLog() ?: MonitorLogEntity()

        val logEntry = MonitorLogEntryEntity(
            beginning = status.start,
            duration = status.duration,
            number = status.number,
            name = getNameFromContacts.invoke(cursor),
            timesQueried = 0 // number of times log entry queried via API - initially it's zero
        )
        val entries = if (log.entries.isEmpty()) {
            listOf(logEntry)
        } else {
            log.copy().entries.toMutableList().plus(logEntry)
        }
        logRepository.insertLog(log.copy(entries = entries))
        Log.d("CallTaskController::addLogEntry", "LogEntry::$logEntry")
    }

    suspend fun addService(name: String, uri: String) {
        val service = MonitorServiceEntity(name = name, uri = uri)
        val rootFromDb = rootRepository.getRoot()
        rootFromDb?.let {
            val services = if (it.services.isEmpty()) {
                listOf(service)
            } else {
                it.copy().services.toMutableList().plus(service)
            }
            rootRepository.insertRoot(it.copy(services = services))
        } ?: run {
            val root = MonitorRootEntity(
                start = DateTime.now().toDateTimeString(),
                services = listOf(service)
            )
            rootRepository.insertRoot(root)
        }
        Log.d("CallTaskController::addService", "Service::$name @ $uri")
    }

    suspend fun insertLog(log: MonitorLogEntity) {
        logRepository.insertLog(log)
    }

    suspend fun getServices() = rootRepository.getRoot()

    suspend fun getStatus() = statusRepository.getStatus()

    suspend fun getLog() = logRepository.getLog()

    suspend fun clearAllTables() {
        rootRepository.deleteRoot()
        logRepository.deleteLog()
        statusRepository.deleteStatus()
    }
}