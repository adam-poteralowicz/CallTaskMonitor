package com.apap.ctm.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.apap.ctm.data.db.BaseListConverter

class MonitorServiceConverter : BaseListConverter<MonitorServiceEntity>(MonitorServiceEntity::class.java)

@Entity
data class MonitorRootEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("start") val start: String?,
    @ColumnInfo("services") val services: List<MonitorServiceEntity>? = emptyList()
)

@Entity
data class MonitorServiceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "uri") val uri: String?
)