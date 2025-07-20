package com.apap.ctm.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.apap.ctm.data.db.BaseListConverter


class MonitorLogEntryConverter : BaseListConverter<MonitorLogEntry>(MonitorLogEntry::class.java)

@Entity
data class MonitorLog(
    @PrimaryKey val id: Int,
    val entries: List<MonitorLogEntry>?
)

@Entity
data class MonitorLogEntry(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "beginning") val beginning: String?,
    @ColumnInfo(name = "duration") val duration: String?,
    @ColumnInfo(name = "number") val number: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "timesQueried") val timesQueried: String?
)