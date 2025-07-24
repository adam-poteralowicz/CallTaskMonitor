package com.apap.ctm.data.mapper

import com.apap.ctm.data.model.MonitorLogEntryEntity
import com.apap.ctm.domain.model.MonitorLogEntry
import javax.inject.Inject

class MonitorLogEntryMapper @Inject constructor()
{

    fun toEntity(entry: MonitorLogEntry) = MonitorLogEntryEntity(
        beginning = entry.beginning,
        duration = entry.duration,
        number = entry.number,
        name = entry.name,
        timesQueried = entry.timesQueried,
    )

    fun toDomain(entity: MonitorLogEntryEntity) = MonitorLogEntry(
        name = entity.name,
        number = entity.number,
        duration = entity.duration,
        beginning = entity.beginning,
        timesQueried = entity.timesQueried
    )
}