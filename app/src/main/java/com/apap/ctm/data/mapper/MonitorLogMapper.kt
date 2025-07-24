package com.apap.ctm.data.mapper

import com.apap.ctm.data.model.MonitorLogEntity
import com.apap.ctm.domain.model.MonitorLog
import javax.inject.Inject

class MonitorLogMapper @Inject constructor(
    private val logEntryMapper: MonitorLogEntryMapper
) {

    fun toEntity(log: MonitorLog) = MonitorLogEntity(
        entries = log.entries.map { logEntryMapper.toEntity(it) }
    )

    fun toDomain(entity: MonitorLogEntity) = MonitorLog(
        entries = entity.entries.map { logEntryMapper.toDomain(it) }
    )
}