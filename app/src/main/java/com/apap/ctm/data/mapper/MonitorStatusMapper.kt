package com.apap.ctm.data.mapper

import com.apap.ctm.data.model.MonitorStatusEntity
import com.apap.ctm.domain.model.MonitorStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MonitorStatusMapper @Inject constructor() {

    fun toEntity(status: MonitorStatus) = MonitorStatusEntity(
        start = status.start,
        stop = status.stop,
        duration = status.duration,
        ongoing = status.ongoing,
        number = status.number,
        name = status.name
    )

    fun toDomain(entity: MonitorStatusEntity) = MonitorStatus(
        number = entity.number,
        ongoing = entity.ongoing,
        duration = entity.duration,
        name = entity.name,
        start = entity.start,
        stop = entity.stop
    )
}