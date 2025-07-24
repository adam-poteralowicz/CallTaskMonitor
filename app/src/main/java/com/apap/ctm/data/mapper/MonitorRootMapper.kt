package com.apap.ctm.data.mapper

import com.apap.ctm.data.model.MonitorRootEntity
import com.apap.ctm.domain.model.MonitorRoot
import javax.inject.Inject

class MonitorRootMapper @Inject constructor(
    private val serviceMapper: MonitorServiceMapper
) {

    fun toEntity(root: MonitorRoot) = MonitorRootEntity(
        start = root.start,
        services = root.services.map { serviceMapper.toEntity(it) }
    )

    fun toDomain(entity: MonitorRootEntity) = MonitorRoot(
        start = entity.start,
        services = entity.services.map { serviceMapper.toDomain(it) }
    )
}