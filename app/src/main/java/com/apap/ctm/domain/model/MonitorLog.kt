package com.apap.ctm.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.apap.ctm.data.db.BaseListConverter

class MonitorLogEntryConverter : BaseListConverter<MonitorLogEntry>(MonitorLogEntry::class.java)

@Entity
data class MonitorLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val entries: List<MonitorLogEntry>? = emptyList()
)

@Entity
data class MonitorLogEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "beginning") val beginning: String?,
    @ColumnInfo(name = "duration") val duration: Int = 0,
    @ColumnInfo(name = "number") val number: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "timesQueried") val timesQueried: Int?
) {
    override fun toString(): String = "$beginning::$duration::$number::$name::$timesQueried"
}