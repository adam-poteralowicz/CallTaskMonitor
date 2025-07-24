package com.apap.ctm.data.mapper

import com.apap.ctm.data.model.MonitorServiceEntity
import com.apap.ctm.domain.model.MonitorService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MonitorServiceMapper @Inject constructor() {

    fun toEntity(service: MonitorService) = MonitorServiceEntity(
        name = service.name,
        uri = service.uri
    )

    fun toDomain(entity: MonitorServiceEntity) = MonitorService(
        name = entity.name,
        uri = entity.uri
    )
}