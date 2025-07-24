package com.apap.ctm.data.mapper

import com.apap.ctm.data.model.MonitorLogEntity
import com.apap.ctm.domain.model.MonitorLog
import javax.inject.Inject

class MonitorLogMapper @Inject constructor(
    private val logEntryMapper: MonitorLogEntryMapper
) {

    fun toEntity(log: MonitorLog) : MonitorLogEntity {
        val mapped = MonitorLogEntity(
            id = log.entries.count(),
            entries = log.entries.map {
                logEntryMapper.toEntity(id = log.entries.indexOf(it), entry = it)
            }
        )
        return mapped
    }

    fun toDomain(entity: MonitorLogEntity?) = MonitorLog(entries = entity?.entries?.map {
        logEntryMapper.toDomain(it)
    } ?: emptyList())
}