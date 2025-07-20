package com.apap.ctm.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.apap.ctm.data.db.BaseListConverter

class MonitorServiceConverter : BaseListConverter<MonitorService>(MonitorService::class.java)

@Entity
data class MonitorRoot(
    @PrimaryKey val id: Int,
    @ColumnInfo("start") val start: String?,
    @ColumnInfo("services") val services: List<MonitorService>?
)

@Entity
data class MonitorService(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "uri") val uri: String?
)