package com.apap.ctm.data.network

import android.database.Cursor
import android.util.Log
import com.apap.ctm.data.repository.MonitorLogRepository
import com.apap.ctm.data.repository.MonitorRootRepository
import com.apap.ctm.data.repository.MonitorStatusRepository
import com.apap.ctm.domain.model.MonitorLog
import com.apap.ctm.domain.model.MonitorLogEntry
import com.apap.ctm.domain.model.MonitorRoot
import com.apap.ctm.domain.model.MonitorService
import com.apap.ctm.domain.model.MonitorStatus
import com.apap.ctm.domain.usecase.GetNameFromContacts
import kotlinx.coroutines.flow.last
import org.joda.time.DateTime
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class CallTaskController @Inject constructor(
    private val logRepository: MonitorLogRepository,
    private val rootRepository: MonitorRootRepository,
    private val statusRepository: MonitorStatusRepository,
    private val getNameFromContacts: GetNameFromContacts
) {

    suspend fun startCall(cursor: Cursor, number: String) {
        val status = MonitorStatus(
            start = DateTime.now(),
            stop = DateTime.now(),
            ongoing = true,
            number = number,
            name = getNameFromContacts.invoke(cursor)
        )
        Log.d("CallTaskController::startCall", "name: ${status.name}, number: ${status.number}")
        statusRepository.insertStatus(status)
    }

    suspend fun stopCall(number: String) {
        val statusFromDb = statusRepository.getStatus().last()
        val duration = (statusFromDb.stop.millis - statusFromDb.start.millis)
            .toDuration(DurationUnit.SECONDS).inWholeSeconds.toInt()
        val status = statusFromDb.copy(ongoing = false, stop = DateTime.now(), duration = duration)
        Log.d("CallTaskController::stopCall", "number: ${status.number}")
        statusRepository.insertStatus(status)
    }

    suspend fun addLogEntry(cursor: Cursor, number: String) {
        val logFromDb = logRepository.getAllLogs().last()[0]
        val log = if (logFromDb.entries?.isEmpty() == true) {
            MonitorLog()
        } else logFromDb
        val logEntry = MonitorLogEntry(
            beginning = DateTime.now(),
            duration = statusRepository.getStatus().last().duration.toString(),
            number = number,
            name = getNameFromContacts.invoke(cursor),
            timesQueried = (log.entries?.find { it.number == number }?.timesQueried?.plus(1)) ?: 1
        )
        val updatedEntries = log.entries?.toMutableList().also {
            it?.plusAssign(logEntry)
        }
        Log.d("CallTaskController::addLogEntry", "LogEntry::$logEntry")
        logRepository.insertLog(log.copy(entries = updatedEntries))
    }

    suspend fun addService(name: String, uri: String) {
        val rootFromDb = rootRepository.getRoot().last()
        val root = if (rootFromDb.services?.isEmpty() == true) {
            MonitorRoot(start = DateTime.now())
        } else rootFromDb
        val service = MonitorService(name = name, uri = uri)
        val updatedServices = root.services?.toMutableList().also {
            it?.plusAssign(service)
        }
        Log.d("CallTaskController::addService", "Service::$name @ $uri")
        rootRepository.insertRoot(root.copy(services = updatedServices))
    }

    fun getServices() = rootRepository.getRoot()

    fun getStatus() = statusRepository.getStatus()

    fun getLog() = logRepository.getAllLogs()

}